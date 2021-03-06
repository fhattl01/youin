# You In?

A scheduling app for friends.

## Instructions for Cloning this Repository

If you decide to clone this repository you will need to setup crashlytics with your own API key. First create a crashlytics account and configure the crashlytics plugin for Android Studio.
 
Next, in order to connect to your  define a string resource file with a property with a string resource named "crashlyticskey". Put your secret crashlytics api key in this resource. The resource file should look like this:

```xml
    <?xml version="1.0" encoding="utf-8"?>
    <resources>
        <string name="crashlyticskey">YOUR-API-KEY-HERE</string>
    </resources>
```

You will also need to define a fabric.properties file with your API secret. The file should look like this:

```
    #Contains API Secret used to validate your application. Commit to internal source control; avoid making secret public.
    #Sat Nov 04 10:52:46 EDT 2017
    apiSecret=YOUR-API-SECRET

```

Both of these files should be included in your gitignore file so you do not accidentally make the API keys public.

Note: In order to build an APK file you must directly include the API key in the manifest. The method above works for testing on an emulator. If you encounter any issues just put the API key in the manifest.