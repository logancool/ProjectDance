package midi;

import javax.sound.midi.MidiMessage;
import javax.sound.midi.SysexMessage;
import processing.core.*; 
import themidibus.*; 

/**
 * Creates a interface between terminal and the keyboard
 * @author logancool
 *
 */
public class MidiManagement extends PApplet {

/*
 * Create the bus to allow data transfer
 */
 int velocity = 0;
MidiBus myBus; 

/*
 * Set up the Gui
 *
 */
public void setup() {
	
	//set the size to be 400x400
	size(400,400);
	
	//set the background to black
	background(0); 
	
	// call the list of available MIDI input and output devices
	MidiBus.list(); 
	
	// Create a new MidiBus object to transfer data
	myBus = new MidiBus(this, 0, 0);
}

public void draw() {
	
	//Set initialize values of the channel pitch and velocity of the key pressed
	int channel = 0;
	int pitch = 0;
	
	
	//Turn the note on
	myBus.sendNoteOn(channel, pitch, velocity);
	delay(200);
	
	//Turn the note off
	myBus.sendNoteOff(channel, pitch, velocity);
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
	
	delay(50002);
}

/**
 * Produces the raw data output of the MIDI device when it is called
 * @param data is the data given from the MIDI
 */


/**
 * Produces the raw data output of the MIDI device when it is called
 * @param data is the data given from the MIDI
 */
public void midiMessage(MidiMessage message) { // You can also use midiMessage(MidiMessage message, String bus_name)
	// Receive a MidiMessage
	println();
	println("Keyboard Information");
	println("--------");
	println("Note Number: "+(int)(message.getMessage()[1] & 0xFF));
	println("Velocity of Note: "+(int)(message.getMessage()[2] & 0xFF));
}
/*
 * delay of messages
 *
 */
public void delay(int time) {
	int current = millis();
	while(millis() < current+time) Thread.yield();
}
  static public void main(String[] passedArgs) {
    String[] appletArgs = new String[] { "MidiManagement" };
    if (passedArgs != null) {
      PApplet.main(concat(appletArgs, passedArgs));
    } else {
      PApplet.main(appletArgs);
    }
  }
}
