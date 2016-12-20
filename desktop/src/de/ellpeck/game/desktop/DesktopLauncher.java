package de.ellpeck.game.desktop;

import com.badlogic.gdx.Application;
import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration;
import de.ellpeck.game.TheGame;

public final class DesktopLauncher{

    public static void main(String[] args){
        LwjglApplicationConfiguration config = new LwjglApplicationConfiguration();
        config.width = 1280;
        config.height = 720;

        LwjglApplication app = new LwjglApplication(new TheGame(), config);
        app.setLogLevel(Application.LOG_DEBUG);
    }
}
