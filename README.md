Download Size Checker
========

**Download Size Checker** - is an application, which provides a functionality to analyze resource for download size by its URL.

## Build and launch
Run in the root of the project the following commands:
```text
mvn clean install -DskipTests=true

java -jar target/download-size-checker-0.0.1-SNAPSHOT.jar
```


## API: 
URL: http://localhost:8080/checker/analyze

Method: GET

Request params: 
- url = target_resource_url (required)
- filter = filter_query_string (optional)

where ***filter_query_string*** is string contains available filters separated with semicolon,
e.g. "img;font;"

#### Supported filter types:

- "*" - all resources will be included;
- "font" - font resources;
- "img" - images, including icons;
- "script" - script resources;
- "style" - stylesheet resources.

========



## Report example
Generates a report similar to the following example:
Request: http://localhost:8080/checker/analyze?url=https://developer.mozilla.org/ru/&filter=img;font

``` json
{
    "rootResourceDetails": {
        "url": "https://developer.mozilla.org/ru/",
        "contentType": "text/html; charset=utf-8",
        "bytesForDownload": 63784
    },
    "totalBytesForDownload": 106427,
    "totalRequestsMade": 7,
    "embeddedResourcesDetails": [
        {
            "url": "https://developer.mozilla.org/static/img/favicon144.e7e21ca263ca.png",
            "contentType": "image/png",
            "bytesForDownload": 1327
        },
        {
            "url": "https://developer.mozilla.org/static/img/favicon114.d526f38b09c5.png",
            "contentType": "image/png",
            "bytesForDownload": 4464
        },
        {
            "url": "https://developer.mozilla.org/static/img/favicon72.cc65d1d762a0.png",
            "contentType": "image/png",
            "bytesForDownload": 907
        },
        {
            "url": "https://developer.mozilla.org/static/img/favicon57.de33179910ae.png",
            "contentType": "image/png",
            "bytesForDownload": 1696
        },
        {
            "url": "https://developer.mozilla.org/static/img/favicon32.7f3da72dcea1.png",
            "contentType": "image/png",
            "bytesForDownload": 441
        },
        {
            "url": "https://developer.mozilla.org/static/fonts/locales/ZillaSlab-Regular.subset.bbc33fb47cf6.woff2",
            "contentType": "font/woff2",
            "bytesForDownload": 33808
        }
    ]
}
```