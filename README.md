# Dialog & Quest

<h3>Backend for game development project "Dialog & Quest"</h3>

<h3>Dialog & Quest is a custom system for Unreal Engine to implement basic
  Dialogs, Quests and Scripts. Dialogs and Quests are imported as CSV files,
  while Scripts are executed from C++ file, so no need to use other scripting languages.
  This makes it easier to add scripts to game with only knowing C++.</h3>

<p>This is a remaster of original system first made in 2016.</p>

# API

<p>This section explains API calls. For mentioned classes please scroll down.</p>

**Project**<br>
***Returns CustomResponse with ProjectDTO as Object***
```
/api/project/getById/{databaseId}
/api/project/getByProjectId/{projectId}
```

# Structure

<h3>This section explains class structure to better understand how this API works.</h3>

## Global

#### GlobalService
<p>Global volatile service. This is used to check condition of multiple values at same time</p>

```
isTrue(boolean ... args)
    Returns true if all input booleans are true

isNull(Object ... args)
    Returns true if any of the provided arguments is null

notNull(Object ... args)
    Returns true if none of the provided objects is null

isBlank(String ... args)
    Returns true if any of the provided strings is blank

isEmpty(List ... args)
    Returns true if any of the provided arrays is empty
```

#### GUIDGenerator
<p>Custom GUID generator. By default generates random string of 16 symbols from 62 random symbols.</p>

```
GUIDGenerator does not need to be imported as it's volatile.
Functions are called with "GUIDGenerator.{function}".
variable definition DEFAULT_LENGTH = 16

generate()                      Calls generate(DEFAULT_LENGTH)

generate({int length})          Generates GUID with given length from 62 random symbols
```

## Classes

#### CustomResponse
<p>CustomResponse class. Used to communicate with frontend.</p>

```
CustomResponse {
    boolean success             Was request a success or not
    String message              Used to pass error messages to frontend
    Object object               Pass any object to frontend
}
```

#### Project
<p>Project class</p>

```
Database table "project"
Project {
    String id                   Database ID as GUID
    String projectId            Used for easier access to project from frontend
    String title                Name of the project
    String description          Developer's description of project
    List<Quest> quests          List of Quests made for this project
    List<Dialog> dialogs        List of Dialogs made for this project
    List<Script> scripts        List of Scripts made for this project
    List<Cue> cues              List of cues (audio files) made for this project
}
```

### Dialog classes

#### Dialog
<p>Dialog class</p>

```
Database table "dialog"
Dialog {
    String id                   Database ID as GUID
    String dialogId             Id defined by user
    List<DialogLine> lines      List of dialog lines
}
```

#### DialogLine
<p>DialogLine class</p>

```
Database table "line"
DialogLine {
    String id                   Database ID as GUID
    String lineId               Id defined by user
    int listOrder               Line's order in array, used for changing line
                                order in frontend for better visualization for user
    String nextLine             Next line's ID (lineId)
    List<DialogLine> choices    List of choices (lineId) for player to say
    Cue cue                     Cue (audio file) assigned to this line for voiceover
    float waitTime              Waiting time before moving to next line, used when there is no cue assigned.
                                If it's empty or 0, user input is required to move to next line.
    List<Script>                List of scripts to be executed when reading this line
    boolean locked              If the line is locked, it will be scriped, script will not execute and moves to next line
    String comment              Developer's comment
}
```

### Quest classes

#### Quest
<p>Quest class</p>

```
Database table "quest"
Quest {
    String id                   Database ID as GUID
    String questId              Id defined by user
    String title                Quest title
    String comment              Developer's comment
    List<QuestPhase> phases     List of QuestPhase
}
```

#### QuestPhase
<p>QuestPhase class</p>

```
Database table "phase"
QuestPhase {
    String id                   Database ID as GUID
    String phaseId              Defined by user, used to reference to phase
    String description          Description provided to player in game
    String comment              Developer's comment
}
```

### Script classes

#### Script
<p>Script class. Similar to what's in C++ system.</p>

```
Database table "script"
Script {
    String id                   Database ID as GUID
    boolean global              Defines if script is globally available,
                                which is used for core functionality of this system.
    String name                 Name of the script function in C++
    List<ScriptVariable>        List of variables required by this script
}
```

#### ScriptVariable
<P>ScriptVariable class</P>

```
Database table "variable"
ScriptVariable {
    String id                   Database ID as GUID
    String variableType         Variable type used in engine
    String variableName         Variable name used in engine
}
```

#### Cue
<p>Cue class. Used for audio files</p>

```
Database table "cue"
Cue {
    String id                   Database ID as GUID
    String cueId                Provided by user
    String comment              Developer's comment
}
```

<br><br><p><i>This system is developed by Kristjan Männimets.</i></p>
