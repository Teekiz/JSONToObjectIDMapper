# JSONtoFileIDMapper
JSONtoFileIDMapper is a utility designed to load all files from a specified directory and assign unique IDs to them based on a provided prefix.
This tool allows you to map files to their respective IDs, making it easier to manage file references in your projects.

## Features:
* Load JSON Files: Loads all JSON files for each directory provided.
* Assign IDs: Assigns IDs to the files with a prefix to identify different file usages.
* File Mapping: Provides functionality to map files to IDs and access them by ID.

## Configuration:
Within your projects root folder, create a properties file. The format for the file is NAME=FILEPATH. The name will be used to construct the prefix and is used as an identifier. Example:
```
products=data/products
```
## Usage:
Once the filepath.properties folder has been created, you can use the JsonIDMapper class within your code. For example:
```
JsonIDMapper jsonIDMapper = new JsonIDMapper("products.properties", 5);
File jsonFile = jsonIDMapper.getFileFromID("PRODU1");
```
Where "example.properties" is the path to your properties file and 3 is the prefix length. For example, using a prefix length of 3, each product would have a prefix of PRO followed by a unique identifier 1 (e.g. PRO1).
