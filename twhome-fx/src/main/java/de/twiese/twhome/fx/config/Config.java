package de.twiese.twhome.fx.config;

import de.twiese.twhome.fx.support.ExecutorManager;
import javafx.scene.paint.Color;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.FileInputStream;
import java.io.IOException;
import java.nio.file.*;
import java.util.*;
import java.util.concurrent.ExecutorService;

import static java.nio.file.StandardWatchEventKinds.ENTRY_MODIFY;

public class Config {

    private static final Logger log = LoggerFactory.getLogger(Config.class);
    private static final List<ChangeAction> actions = new ArrayList<>();
    private static Config config;
    private final String configLocation;
    private final Properties defaultProperties;
    private final Properties properties;
    private boolean initialReload = true;

    private Config(String configLocation) throws IOException {
        this.configLocation = configLocation;
        this.defaultProperties = new Properties();
        this.properties = new Properties();
        defaultProperties.load(getClass().getClassLoader().getResourceAsStream("default-config.properties"));
        config = this;
        reload();
    }

    public synchronized static void init(String configLocation) throws InstantiationException, IOException {
        if (config != null) {
            throw new InstantiationException("Config already exists");
        }
        config = new Config(configLocation);
    }

    public static String getProperty(String key) {
        if (config.properties.containsKey(key)) {
            return config.properties.getProperty(key);
        } else {
            return config.defaultProperties.getProperty(key);
        }
    }

    public static Color getColorProperty(String key) {
        String color = getProperty(key);
        return Color.web(color);
    }

    public static int getIntProperty(String key) {
        String intString = getProperty(key);
        return Integer.parseInt(intString);
    }

    public static boolean getBooleanProperty(String key) {
        String booleanString = getProperty(key);
        return Boolean.parseBoolean(booleanString);
    }

    public static void onConfigChange(ChangeAction action) {
        actions.add(action);
    }

    public void reload() throws IOException {
        if (configLocation != null && configLocation.length() > 0) {
            if (configLocation.startsWith("classpath:")) {
                log.info("Load configuration from classpath: " + configLocation);
                properties.load(getClass().getClassLoader().getResourceAsStream(configLocation.replace("classpath:", "")));
            } else if (configLocation.startsWith("http://") || configLocation.startsWith(("https://"))) {
                throw new UnsupportedOperationException("Load configuration by http is not yet implemented");
            } else {

                properties.load(new FileInputStream(configLocation));
                if (initialReload) {
                    watchFile(configLocation);
                    log.info("Loaded configuration from file: " + configLocation);
                } else {
                    log.info("Reloaded configuration from file: " + configLocation);
                }
            }
        } else {
            log.warn("No configLocation is set. Using default configuration.");
        }

        TimeZone.setDefault(TimeZone.getTimeZone(getProperty("timezone")));
        Locale.setDefault(new Locale(getProperty("localeLanguage"), getProperty("localeCountry")));

        initialReload = false;
    }

    private void reloadSilently() {
        try {
            reload();
        } catch (IOException e) {
        }
    }

    private void watchFile(String filename) throws IOException {
        WatchService watcher = FileSystems.getDefault().newWatchService();
        Paths.get(filename).getParent().register(watcher, ENTRY_MODIFY);
        ExecutorService watchExecutor = ExecutorManager.createExecutorDaemon();
        try {
            watchExecutor.submit(() -> {
                while (true) {
                    WatchKey key = watcher.take();
                    key.pollEvents().forEach(event -> {
                        key.pollEvents();
                        reloadSilently();
                        actions.forEach(ChangeAction::execute);
                    });
                    key.reset();
                }
            });
        } catch (ClosedWatchServiceException e) {
        }
    }

    @FunctionalInterface
    public interface ChangeAction {
        void execute();
    }
}
