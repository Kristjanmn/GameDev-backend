# Dialog & Quest

## This is NOT a game! This is backend for companion app, which is meant to help with game development!<br>

<h3>Backend for game development project "Dialog & Quest"</h3>

<h3>Dialog & Quest is a custom system for Unreal Engine to implement basic
  Dialogs, Quests and Scripts. Dialogs and Quests are imported as CSV files,
  while Scripts are executed from C++ file, so no need to use other scripting languages.
  This makes it easier to add scripts to game with only knowing C++.</h3>

<p>This is a remaster of original system first made in 2016.</p>

# API

<p>This section explains API calls. For mentioned classes please scroll down.</p>
<p><b>All responses are CustomResponse, any returned object is in CustomResponse.object</b></p>

**Project**<br>
```
// Next 2 also set the incoming cookie PROJECTID
/api/project/getById/{databaseId}                   [GET]
/api/project/getByProjectId/{projectId}             [GET]
/api/project/checkAvailable/{projectId}             [GET]

// Save project
/api/project/saveProject (post ProjectDTO)          [POST]  [Cookie: PROJECTID = projectId]
```

**Dialog**<br>
```
// Simplified method of getting all Dialogs associated with project.
/api/dialog/                                        [GET]   [Cookie: PROJECTID = projectId]
/api/dialog/getById/{databaseId}                    [GET]
/api/dialog/getByDialogId/{dialogId}                [GET]
/api/dialog/getByProject/{projectDatabaseId}        [GET]
```

**Quest**<br>
```
// Simplified method of getting all Quests associated with project.
/api/quest/                                         [GET]   [Cookie: PROJECTID = projectId]
/api/quest/getById/{questDatabaseId}                [GET]   [Cookie: PROJECTID = projectId]
/api/quest/getByProject/{projectDatabaseId}         [GET]

// Check if an questID is available or already used in this project.
/api/quest/checkIdAvailable/{questId}                   [GET]   [Cookie: PROJECTID = projectId]
/api/quest/checkIdAvailable/{questDatabaseId}/{questId} [GET]   [Cookie: PROJECTID = projectId]

// Save quest
/api/quest/saveQuest/ (post Quest)                  [POST]  [Cookie: PROJECTID = projectId]
```

**Script**<br>
```
// Simplified method of getting all Scripts associated with project.
/api/script/                                        [GET]   [Cookie: PROJECTID = projectId]
/api/script/getById/{scriptDatabaseId}              [GET]   [Cookie: PROJECTID = projectId]
/api/script/getByProject/{projectDatabaseId}        [GET]
/api/script/checkIdAvailable/{scriptName}           [GET]   [Cookie: PROJECTID = projectId]
/api/script/saveScript (post ScriptDTO)             [POST]  [Cookie: PROJECTID = projectId]
```

**Cue**<br>
```
// Simplified method of getting all Cues associated with project.
/api/cue/                                           [GET]   [Cookie: PROJECTID = projectId]
/api/cue/getByProject/{projectDatabaseId}           [GET]
/api/cue/checkIdAvailable/{cueId}                   [GET]   [Cookie: PROJECTID = projectId]
/api/cue/saveCue/ (post Cue)                        [POST]  [Cookie: PROJECTID = projectId]
```

# Structure

<h3>This section explains class structure to better understand how this API works.</h3>

## Global

#### GlobalService
<p>Global volatile service. This is used to check condition of multiple values at same time</p>

```
String[] reservedIds
    List of reserved IDs, which are not to be given to any user defined IDs

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

equalsAnyString(String compareTo, String ... args)
    Returns true if any input string equals compareTo string.
    Case insensitive.
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
<p>ProjectDTO class</p>

```
ProjectDTO {
    String id
    String projectId
    String title
    String description
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
    String comment              Developer's comment
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
<p>Script class.<br>
Similar to what's in C++ system.</p>

```
Database table "script"
Script {
    String id                   Database ID as GUID
    boolean global              Defines if script is globally available,
                                which is used for core functionality of this system.
    String name                 Name of the script function in C++
    List<ScriptVariable>        List of variables required by this script
    String comment              Developer's comment
}
```
<p>ScriptDTO class</p>

```
ScriptDTO {
    String id
    boolean global
    String name
    List<ScriptVariableDTO> variables
    String comment
}
```
<p>ScriptIndex class<br>
Used in script lines to ensure correct order of execution.</p>

```
Database table "script_index"
ScriptIndex {
    String id                   Database ID as GUID
    Script script               Indexed Script
    int zOrder                  Script's index in array
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
<p>ScriptVariableDTO class</p>

```
ScriptVariableDTO {
    String type
    String name
}
```
<p>ScriptVariableIndex class<br>
Used to make sure Script variables are in correct order.</p>

```
Database table "variable_index"
ScriptVariableIndex {
    String id                   Database ID as GUID
    ScriptVariable variable     Script variable
    int zOrder                  Variable's index in Script
}
```

#### Cue
<p>Cue class.<br>
Used for audio files</p>

```
Database table "cue"
Cue {
    String id                   Database ID as GUID
    String cueId                Provided by user
    String comment              Developer's comment
}
```

<br><br><p><i>This system is developed by Kristjan Männimets.</i></p>
