package ana.fx.app.launcher;


// dep

import ana.fx.app.App;
import ana.fx.canvas.draw.Draw;
import ana.fx.window.WindowConfig;
import ana.fx.canvas.Canvas;
import ana.lang.fx.LauncherSequenceException;

import javafx.animation.AnimationTimer;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.stage.Stage;


// main

public abstract class Launcher<AppType extends App> extends Application {
    // user init

    public abstract AppType initApp(AppType.Init init);

    public WindowConfig initWindow() {
        return WindowConfig.WINDOW_CFG_DEFAULT;
    }

    // start

    @Override
    public void start(Stage stage) {
        WindowConfig cfg = initWindow();
        Info info = newLaunchInfo(cfg, stage);
        AppType app = newApp(info);

        RuntimeException toThrow = new LauncherSequenceException("start");

        try { preLaunch(app, info); }
        catch (Exception e) { throw toThrow; }

        try { launch(app, info); }
        catch (Exception e) { throw toThrow; }

        try { postLaunch(app, info); }
        catch (Exception e) { throw toThrow; }
    }

    // one-time events

    protected void preLaunch(AppType app, Info info) throws Exception {}

    protected void launch(AppType app, Info info) throws Exception {
        setScene(info);
        doEventLoop(app, info);
    }

    protected void postLaunch(AppType app, Info info) throws Exception {}

    // looped events

    protected void preEvent(AppType app, Info info) throws Exception {}

    protected void mainEvent(AppType app, Info info) throws Exception {
        app.eventLoop();
    }

    protected void preRenderEvent(AppType app, Info info) throws Exception {}

    protected void renderEvent(AppType app, Info info) throws Exception {
        app.canvas.drawFrame();
    }

    // loop init

    protected AnimationTimer doEventLoop(AppType app, Info info) {
        RuntimeException toThrow = new LauncherSequenceException("event loop");

        return doLoop(
            // pre-event
            () -> {
                try { preEvent(app, info); }
                catch(Exception e) { throw toThrow; }
            },
            // main
            () -> {
                try { mainEvent(app, info); }
                catch(Exception e) { throw toThrow; }
            },
            // pre-render
            () -> {
                try { preRenderEvent(app, info); }
                catch(Exception e) { throw toThrow; }
            },
            // render
            () -> {
                try { renderEvent(app, info); }
                catch(Exception e) { throw toThrow; }
            }
        );
    }

    protected static AnimationTimer doLoop(Runnable... events) {
        AnimationTimer at = new AnimationTimer() {
            @Override
            public void handle(long l) {
                for (Runnable e : events) {
                    e.run();
                }
            }
        }; at.start();

        return at;
    }

    // set

    protected void setScene(Info info) { // nonstatic for generics
        info.stage.setScene(info.scene);
        info.stage.setTitle(info.cfg.title);
        info.stage.setResizable(false);

        info.stage.setX(info.cfg.pos.x());
        info.stage.setY(info.cfg.pos.y());

        info.stage.show();
    }

    // new

    protected static Info newLaunchInfo(WindowConfig cfg, Stage stage) {
        Canvas canvas = newCanvas(cfg, stage);
        Scene scene = newScene(cfg, stage, canvas);

        return new Info(
            cfg, canvas, stage, scene
        );
    }

    protected AppType newApp(Info info) {
        App.Init init = new AppType.Init(info.canvas);
        return initApp(init);
    }

    protected static Canvas newCanvas(WindowConfig cfg, Stage stage) {
        Draw draw = new Draw();
        return new Canvas(cfg, draw);
    }

    protected static Scene newScene(WindowConfig cfg, Stage stage, Canvas canvas) {
        return new Scene(canvas, cfg.size.x(), cfg.size.y());
    }

    // inner util

    protected record Info(WindowConfig cfg, Canvas canvas, Stage stage, Scene scene) {}


    // --


    // reading material
    // https://stackoverflow.com/questions/75175/create-instance-of-generic-type-in-java/25195050#25195050
}
