import processing.core.*; 
import processing.data.*; 
import processing.opengl.*; 

import themidibus.*; 
import javax.sound.midi.MidiMessage; 
import javax.sound.midi.SysexMessage; 
import javax.sound.midi.ShortMessage; 

import themidibus.*; 

import java.applet.*; 
import java.awt.Dimension; 
import java.awt.Frame; 
import java.awt.event.MouseEvent; 
import java.awt.event.KeyEvent; 
import java.awt.event.FocusEvent; 
import java.awt.Image; 
import java.io.*; 
import java.net.*; 
import java.text.*; 
import java.util.*; 
import java.util.zip.*; 
import java.util.regex.*; 

public class AdvancedMIDIMessageIO extends PApplet {

 //Import the library
 //Import the MidiMessage classes http://java.sun.com/j2se/1.5.0/docs/api/javax/sound/midi/MidiMessage.html



MidiBus myBus; // The MidiBus

public void setup() {
	size(400,400);
	background(0);
	
	MidiBus.list(); // List all available Midi devices on STDOUT. This will show each device's index and name.
	myBus = new MidiBus(this, 0, 0); // Create a new MidiBus object
}

public void draw() {
	int channel = 0;
	int pitch = 64;
	int velocity = 127;
	
	myBus.sendNoteOn(channel, pitch, velocity); // Send a Midi noteOn
	delay(200);
	myBus.sendNoteOff(channel, pitch, velocity); // Send a Midi nodeOff
	delay(100);

	//Or for something different we could send a custom Midi message ...
	
	int status_byte = 0xA0; // For instance let us send aftertouch
	int channel_byte = 0; // On channel 0 again
	int first_byte = 64; // The same note;
	int second_byte = 80; // But with less velocity
		
	myBus.sendMessage(status_byte, channel_byte, first_byte, second_byte);
	
	//Or we could even send a variable length sysex message
	
	myBus.sendMessage(new byte[] {(byte)0xF0, (byte)0x1, (byte)0x2, (byte)0x3, (byte)0x4, (byte)0xF7});
	//We could also do the same thing this way ...
	
	try { //All the methods of SysexMessage, ShortMessage, etc, require try catch blocks
		SysexMessage message = new SysexMessage();
		message.setMessage(0xF0, new byte[] {(byte)0x5, (byte)0x6, (byte)0x7, (byte)0x8, (byte)0xF7}, 5);
		myBus.sendMessage(message);
	} catch(Exception e) {
		
	}
	
	delay(2000);
}

// Notice all bytes below are converted to integeres using the following system:
// int i = (int)(byte & 0xFF) 
// This properly convertes an unsigned byte (MIDI uses unsigned bytes) to a signed int
// Because java only supports signed bytes, you will get incorrect values if you don't do so

public void rawMidi(byte[] data) { // You can also use rawMidi(byte[] data, String bus_name)
	// Receive some raw data
	// data[0] will be the status byte
	// data[1] and data[2] will contain the parameter of the message (e.g. pitch and volume for noteOn noteOff)
	println();
	println("Raw Midi Data:");
	println("--------");
	println("Status Byte/MIDI Command:"+(int)(data[0] & 0xFF));
	// N.B. In some cases (noteOn, noteOff, controllerChange, etc) the first half of the status byte is the command and the second half if the channel
	// In these cases (data[0] & 0xF0) gives you the command and (data[0] & 0x0F) gives you the channel
	for(int i = 1;i < data.length;i++) {
		println("Param "+(i+1)+": "+(int)(data[i] & 0xFF));
	}
}

public void midiMessage(MidiMessage message) { // You can also use midiMessage(MidiMessage message, String bus_name)
	// Receive a MidiMessage
	// MidiMessage is an abstract class, the actual passed object will be either javax.sound.midi.MetaMessage, javax.sound.midi.ShortMessage, javax.sound.midi.SysexMessage.
	// Check it out here http://java.sun.com/j2se/1.5.0/docs/api/javax/sound/midi/package-summary.html
	println();
	println("MidiMessage Data:");
	println("--------");
	println("Status Byte/MIDI Command:"+message.getStatus());
	for(int i = 1;i < message.getMessage().length;i++) {
		println("Param "+(i+1)+": "+(int)(message.getMessage()[i] & 0xFF));
	}
}

public void delay(int time) {
	int current = millis();
	while(millis() < current+time) Thread.yield();
}
  static public void main(String[] passedArgs) {
    String[] appletArgs = new String[] { "AdvancedMIDIMessageIO" };
    if (passedArgs != null) {
      PApplet.main(concat(appletArgs, passedArgs));
    } else {
      PApplet.main(appletArgs);
    }
  }
}
