The data design I chose was the use an ArrayList of ArrayList of Note. A 
Note contains information such as the pitch (which is an enum of the standard
12), octave, startBeat, endBeat and volume. The ArrayList of Note is
all of the notes played at a single beat. Then the ArrayList of ArrayList 
of Note are the Arrays of each beat and their corresponding Notes.
The Music Editor has a Musical board which is the nested arrays, 
the current beat, the tempo, the lowest octave, the lowest note of the 
lowest octave, the highest octave and the highest note of the highest octave. 
Notes that take up more than one beat are represented by an identical 
reference in the corresponding position in the ArrayList. This way, one 
mutation changes all of them and the Note itself can be picked at from any 
point in its range. 
The Note class handles invariants such as negative values or changing a 
start time to after the end time. The upper level Music Editor handles 
things such as removing references that are no longer needed and delegating 
checking whether or not a change will cause an overlap. 


ABOVE IS ORIGINAL OUTLINE:
Changes include:
Everything is more generic e.g. now List<Collection<AbstractNote>>
No longer pass aliases to important data
Added static factory methods to build a model
Changed visibility of several methods and fields to be more protected
Note constructors now include volume and instrument
Protected against null NoteType
Added invariants to volume and instruments and pitch
Fixed equality and hashcode methods to account for changes
Made updaterange more accurate
Overlapped notes now weave notes together rather than denying them
(Will probably change the latter again in the future)

VIEW DESIGN:
We have a ViewModel that is a class whose field is a MusicEditorModel
The only methods that can get to the model are tightly controlled
The ViewModel has three methods that all draw the Model differently
"console"
"gui"
"midi"
Each of those is a class of their own that has to go through the ViewModel
to get to the Model Data
