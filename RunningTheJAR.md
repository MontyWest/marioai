Mario AI - Evolutionary Agent
=============================

Running the JAR
---------------

This directory (containing this README) should contain the jar file: marioai-full.  
If not check the montywest-marioai/target/ folder  
If not you can build it with Maven from the montywest-marioai/ folder  

### Learning

`java -jar marioai-full.jar -mode learn -file {parameterFile}`  
  
Some example parameter files are included in the params/ folder

### Watching/Eval

`java -jar marioai-full.jar -mode {eval|watch} [ai-options] [engine-options]`  
  
Choosing `eval` will just print the score, choosing `watch` will open a window with Mario playing the levels.

### [ai-options]

* `-params {fromFile|comp|default}`
	`fromFile` defaults to params/demo.params  
	`comp` is the comparator task  
	`default` is one level, default options  

* `-paramsFile *pathToFile*`
	Some example parameter files are included in params/

* `-agent {*pathToAgent*|human}`
	Some example agents are included in the agents/ folder  
	If left blank defaults to the agent/complex.agent  
	`human` allows Mario to be controlled by the arrow keys and `a` and `s` keys

* `-seed *number*`
	Allows you to set the level generation seed

* `-nol *number*`
	Allows you to set the number of levels if no params are set

* `-outFile *fileName*`
	Prints run statistics to given fileName, deafults to eval-task.out

### [engine-options]

Native to the Mario AI benchmark, some examples:

* `-fps *number*`
	Sets the frames per second

* `-i on`
	Makes Mario invincible

More can be found in the ParameterContainer class  
montywest-marioengine/src/main/java/ch/idsia/utils/ParameterContainer class

