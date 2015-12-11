====== README =========

~~ Original Version ; Amendments Found Below ~~

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


~~ Change Log ; Assignment 6 ~~

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


~~ Change Log ; Assignment 7 ~~

Here is our final implementation of the Music Editor.

The main arguments still do not have a particular order they need to be written
in, so long as they are valid inputs. Interacting with the controller is as follows:

These keys work on their own:
 - ......... use the arrow keys to traverse the piece
 - "space" . used for play/pause
 - "home" .. returns the view to the beginning of the piece
 - "end" ... returns the view to the end of the piece
These keys work as switches:
 - "a" ..... toggles the addNote mode
 - "w" ..... toggles the addNote mode, for percussion notes
 - "s" ..... toggles the removeNote mode
 - "d" ..... toggles the changeNoteStart mode
 - "f" ..... toggles the changeNoteEnd mode
 - "q" ..... toggles the moveNote mode
 - "e" ..... toggles the changeCurBeat mode, to pick where the music begins
 - "v" ..... toggles the expandBoard mode
By using these switches, mouse clicks are used to interact with the musical data.
These keys:
 - "t" ..... when in the expandBoard mode, will add the next highest octave
 - "g" ..... when in the expandBoard mode, will add the next lowest octave
 - "b" ..... when in the expandBoard mode, will add 8 more beats to the piece
also work in tandem with the "v" key, much like the other toggle keys with the
mouse. For all modes that are mutating existing notes, the first click will be
used to select the note, and the second will provide the mutation of the data.

Sometimes the clicking can be buggy, so it may take two or three clicks on occasion
to perform the desired operation. While the program is running, all interactions
with the program are printed into the console, so if there is ever trouble the user
should watch the outputs to help guide their interactions.

Several changes were needed in order to implement the Playback View.
First, a Swing timer was created in the GuiController that ticks every tempo beat.
Every tick runs a Action Performed class that ticks the current beat and delegates to the views.
Both the Playback View and its GuiView field have current beat fields that are increased by the Timer.

When they are ticked, the GuiView repaints things such as the red line based on the current beat and
the cell size which is set by a static variable (currently 30 pixels). Any methods called on the Playback view involving changing graphics are delegated to the internal GuiView who is in charge of repainting everything.

In the Playback view, a new MIDI class was created.
We decided to create a new MIDI view because this one was radically different from our view from a previous assignment.
Instead of breaking the other one, we wanted to keep both the Playback view and MIDI view fully functional
separately. After creating the new MIDI view in the Playback view class, we found that not a lot of code
was repeated because many things were changed in order to tick beat by beat rather than dumping all of the
notes. Keeping the original view also helped with making sure the sound remained the same seeing that an
external Timer was now in charge of playing notes rather than the MIDI itself.

Some specific design changes made to the Editor view include:
-Added several fields with aliases to different JComponents to better access them without having to travel
through all of the children of the final JFrame.
-Each cell is in charge of painting itself background by using stored coordinates and finding out if there is a note at its location.
-The editor view now creates an empty default board when the model is empty rather than throwing an exception

An larger design change was making the ViewModel abstract. In order to make the construction of the abstract view
to work without creating a concrete class, we had to Override a method, but now, nothing relies on a concrete
ViewModel.

We also added interfaces such as GuiSpecifcController in order to distinguish between classes that had a more
specialized role. It didn't make sense for the Controller running the console and MIDI view to have methods
meant for delegating a GUI to scroll.

In order to clean up our Main method, we made a MainController that took the String[] args from the Main method and created and ran the appropriate classes.

We also changed several classes to support the intake of an Appendable in order to test. When a class is
initialized with an Appendable, we put it into a "test mode." When testing, rather than producing visual output,
it adds messages to the Appendable at key points so that we can compare it to the expected output in order to
see if everything is created like we expected. The testOutput text file contains a massive output from drawing a
default board. This 1000+ line string didn't work in a test without hanging for a time, but it did create the
expected output when JUnit got around to completing.

~~ Other Notes ~~

Obviously, there are many things we would have done differently looking back.
Given sufficient time, here are the changes we would have made:
- in the GuiController, when loading the pressedEvents for keys a/s/d/f/w/e/r (the
  ones that act as toggle switches for various interactive modes), there is most
  likely a way to abstract out the repetitive code, since the only thing changing
  between the Runnables is the keycode. At first thought, this could easily be done
  using a Consumer that takes in an int to represent the key's code, although this
  would not only require a separate HashMap within the InputHandler that includes
  Consumers and note Runnables, but it would also make every key a toggle switch.
  There is clearly a solution for this, but our limited time was better spent
  elsewhere given that it works as is.
- in an almost identical situation, the mouseClicked() method within the InputHandler
  is quite large/confusing/disgusting, so abstraction would definitely improve its
  readability. This could also be done by using a HashMap of key codes (the
  keyPressed field in the controller) and Consumers<Posn>, stored in the controller
  itself to keep the InputHandler more generic/adaptable. This would help reduce the
  code within the mouseClicked() method, and would be generally more sophisticated.
  Again, however, our limited time was better spent elsewhere given that it works
  as is.
