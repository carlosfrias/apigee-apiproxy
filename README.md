# Apigee API Proxy Management Scripts

##Overview

This is a set of groovy scripts that help manage the development cycle I found when working with 
the Apigee Edge server in the cloud. I found that the emphasis on using curl from bash was good but 
it also introduces more manual interaction then is really required. The maven plugin does a good 
job of managing deployments but I did not find the fine grain control that helped me understand how 
to work with Edge. I found myself writing scripts to reduce the unnecessary manual interactions and 
to gain greater control over the development cycle. I wanted the flexibility of working with a shell 
and the opportunity to automate interactions beyond what I received from working with browser 
extensions. I chose to use groovy and the groovy shell as an extension to my development environment. 

During this process I ended up with a micro framework of scripts that give me the level of control 
I was looking for while still reducing an in some cases removing unnecessary manual steps. 

## Usage
The gradle shell plugin is used to make available any classpath dependencies. The apiproxySetup.groovy 
script in the src/main/scripts folder can be consulted initially.
 
 

