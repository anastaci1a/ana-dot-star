package ana.fx.app.launcher;


// dep

import ana.fx.app.App;
import ana.fx.app.AppX;
import ana.io.device.DeviceInput;


// main

public abstract class LauncherX<AppType extends AppX> extends Launcher<AppType> {
    // user init

    public abstract AppType initApp(AppType.Init init); // enforce type (? extends AppX).Init

    @Override
    public final AppType initApp(App.Init init) { return null; }

    // looped events

    @Override
    protected void preEvent(AppType app, Info info) throws Exception {
        super.preEvent(app, info);
        app.input.frameStart();
    }

    // new

    @Override
    protected AppType newApp(Info info) {
        DeviceInput input = newInput(info);
        AppX.Init init = new AppType.Init(info.canvas(), input);
        return initApp(init);
    }

    protected DeviceInput newInput(Info info) {
        return DeviceInput.listener(info.stage(), info.scene());
    }
}

