# Analytics Framework #
   
Before running the project, you should configure the Google Cloud Credentials following the instructions below.  
After configuration, you should be able to run the application through command line.    
If you are still unable to do that, you should close the IDE and then reopen the project.
    
## Configure the Google Cloud Credentials ##
1. Downloads the service account JSON file located in the backend/src/main/resources folder.
2. Passing credentials via environment variable   
Here is the instruction provided by Google Cloud:   
Set the variable in your shell startup file, for example in the ~/.bashrc or ~/.profile file.
* Linux or MacOS  
```
  export GOOGLE_APPLICATION_CREDENTIALS="KEY_PATH"
```
Replace KEY_PATH with the path of the JSON file that contains your service account key.   
For example:    
```
  export GOOGLE_APPLICATION_CREDENTIALS="/home/user/Downloads/service-account-file.json"
```

* Windows
For PowerShell:
```
  $env:GOOGLE_APPLICATION_CREDENTIALS="KEY_PATH"
```
For example:
```
  $env:GOOGLE_APPLICATION_CREDENTIALS="C:\Users\username\Downloads\service-account-file.json"
```
For command prompt:
```
  set GOOGLE_APPLICATION_CREDENTIALS=KEY_PATH
```
Replace KEY_PATH with the path of the JSON file that contains your service account key.    
   
      
## Starting the project ##
### Set up Backend and Frontend Server ###
```
mvn install
```
   
Run the Java backend by typing
```
mvn exec:exec
```
in the backend folder   
   
This will start a server at http://localhost:8080/    
    
## API Document ##
To extend the framework, you should follow the plugin interfaces as below.   
   
Two types of plugin:   
### 1. DataPlugin ###
Should implement the following methods:      
    
```
void onRegister(AnalyticsFramework framework);
```
Description:   
Called (only once) when the plug-in is first registered with the
framework, giving the plug-in a chance to perform any initial set-up
before the analytics has begun (if necessary).
@param framework The {@link AnalyticsFramework} instance with which the 
plug-in was registered.
    
```
String getAnalyticsName();
```
Description:    
Gets the name of the plug-in analytics. @return analytics name.
   
```
void onNewAnalytics();
```
Description:    
Called when a new analytics process is about to begin.
   
```
List<Item> getItems();
```
Description:    
Gets a list of items of the plug-in analytics. The plug-in analytics should convert any data source to
a defined data structure - Item.   
@return a list of items.   
   
```
boolean isCompleted();
```
Description:    
Returns true if the analytics is over. Returns false otherwise.
   
```
String getCompletedMessage();
```
Description:    
Returns the message to display when the analytics is over.    
    
### 2. VisualizationPlugin ###
Should implement the following methods:   
   
```
void onRegister(AnalyticsFramework framework);
```
Description:    
Called (only once) when the plug-in is first registered with the framework, giving the plug-in a chance to perform any initial set-up
before the analytics has begun (if necessary). @param framework The {@link AnalyticsFramework} instance with which the
plug-in was registered.   
    
```
String getVisualizationName();
```
Description:    
Gets the name of the plug-in visualization. @return analytics name.   
     
```
void onNewVisualization();
```
Description:    
Called when a new visualization process is about to begin.   
    
```
String draw(List<ProcessedItem> processedItems);
```
Description:    
Performs data visualization display. @param processedItems The list of {@link ProcessedItem} instances from which the plug-in draw visualization.   
      

### Data Input ###
The data plugin should convert input data into Item object by creating a new Item instance:
```
Item(String text, String date, String time)
```

### Data Output ###
The visualization plugin should perform visualization using ProcessedItem:
```
ProcessedItem(String text, String date, String time, float score, float magnitude)
```

## Data Processing ##
This framework utilizes Google Cloud's Natural Language API to perform sentiment analysis on text from various sources (provided by data plugins) and
shows results in various ways (using visualization plugins). This framework performs the sentiment analysis itself, thus providing reuse benefit.   
Also, this framework provides visualization for reuse purpose.    
    
## Plugin Register ## 
Once you implement a new plugin, you should add them in the corresponding file located in META-INF.services folder.      
You should add each new plugin by following the rule declared in each file.  