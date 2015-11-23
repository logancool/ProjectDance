/**
    Drone Listener
*/
package com.run;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.event.InputEvent;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.awt.image.BufferedImage;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;

import com.shigeodayo.ardrone.ARDrone;
import com.shigeodayo.ardrone.navdata.AttitudeListener;
import com.shigeodayo.ardrone.navdata.BatteryListener;
import com.shigeodayo.ardrone.navdata.DroneState;
import com.shigeodayo.ardrone.navdata.StateListener;
import com.shigeodayo.ardrone.navdata.VelocityListener;
import com.shigeodayo.ardrone.video.ImageListener;

public class DroneListener extends JFrame {
	private static final long serialVersionUID = 1L;

	private ARDrone ardrone = null;

	private MyPanel myPanel = null;

	public DroneListener() {
		initialize();
	}

	private void initialize() {
		ardrone = new ARDrone("192.168.1.1");
		System.out.println("connect drone controller");
		ardrone.connect();
		System.out.println("connect drone navdata");
		ardrone.connectNav();
		System.out.println("start drone");
		ardrone.start();

		ardrone.addImageUpdateListener(new ImageListener() {
			@Override
			public void imageUpdated(BufferedImage image) {
				if (myPanel != null) {
					myPanel.setImage(image);
					myPanel.repaint();
				}
			}
		});

		ardrone.addAttitudeUpdateListener(new AttitudeListener() {
			@Override
			public void attitudeUpdated(float pitch, float roll, float yaw,
					int altitude) {
				//System.out.println("pitch: " + pitch + ", roll: " + roll
					//	+ ", yaw: " + yaw + ", altitude: " + altitude);
			}
		});

		ardrone.addBatteryUpdateListener(new BatteryListener() {
			@Override
			public void batteryLevelChanged(int percentage) {
				//System.out.println("battery: " + percentage + " %");
			}
		});

		ardrone.addStateUpdateListener(new StateListener() {
			@Override
			public void stateChanged(DroneState state) {
				//System.out.println("state: " + state);
			}
		});

		ardrone.addVelocityUpdateListener(new VelocityListener() {
			@Override
			public void velocityChanged(float vx, float vy, float vz) {
				//System.out.println("vx: " + vx + ", vy: " + vy + ", vz: " + vz);
			}
		});
		addKeyListener(new KeyAdapter() {
			public void keyReleased(KeyEvent e) {
				ardrone.stop();
			}

			public void keyPressed(KeyEvent e) {
				int key = e.getKeyCode();
				int mod = e.getModifiersEx();

				boolean shiftflag = false;
				if ((mod & InputEvent.SHIFT_DOWN_MASK) != 0) {
					shiftflag = true;
				}

				switch (key) {
				case KeyEvent.VK_ENTER:
					ardrone.takeOff();
					break;
				case KeyEvent.VK_SPACE:
					ardrone.landing();
					break;
				case KeyEvent.VK_Q:
					ardrone.stop();
					break;
				case KeyEvent.VK_RIGHT:
					if (shiftflag) {
						ardrone.move3D(0, 0, 0, 30); // higher then number the more steep
						shiftflag = false;
					} else
						ardrone.move3D(0, 30, 0, 0);
					break;
				case KeyEvent.VK_LEFT:
					if (shiftflag) {
						// ardrone.spinRight();
						ardrone.move3D(0, 0, 0, -30);
						shiftflag = false;
					} else
						ardrone.move3D(0, -30, 0, 0);
					// ardrone.goRight();
					break;
				case KeyEvent.VK_DOWN:
					if (shiftflag) {
						// ardrone.up();
						ardrone.move3D(0, 0, -30, 0);
						shiftflag = false;
					} else
						ardrone.move3D(30, 0, 0, 0);
					// ardrone.forward();
					break;
				case KeyEvent.VK_UP:
					if (shiftflag) {
						// ardrone.down();
						ardrone.move3D(0, 0, 30, 0);
						shiftflag = false;
					} else
						ardrone.move3D(-30, 0, 0, 0);
					// ardrone.backward();
					break;
				case KeyEvent.VK_A:
					// ardrone.spinRight();
					ardrone.move3D(0, 0, 0, 100);
					break;
				case KeyEvent.VK_D:
					// ardrone.spinLeft();
					ardrone.move3D(0, 0, 0, -100);
					break;
				case KeyEvent.VK_W:
					// ardrone.up();
					ardrone.move3D(0, 0, -30, 0);
					break;
				case KeyEvent.VK_S:
					// ardrone.down();
					ardrone.move3D(0, 0, 30, 0);
					break;
				case KeyEvent.VK_E:
					ardrone.reset();
					break;
				case KeyEvent.VK_Z:
					ardrone.move3D(10, 10, 10, 10);
					break;
				case KeyEvent.VK_X:
					ardrone.move3D(-10, -10, -10, -10);
					break;
				case KeyEvent.VK_C:
					ardrone.move3D(10, 10, 0, 0);
					break;
				case KeyEvent.VK_V:
					ardrone.move3D(-10, -10, 0, 0);
					break;
				}
			}
		});
		
		this.setTitle("ardrone");
		this.setSize(400, 400);
		this.add(getMyPanel());
	}
	
	
	public boolean noteListener(int note){
		System.out.println("note:"+ note);
		if (note == 41){
			System.out.println("-------------------------------TAKING OFFFFFFFFFF!!!!!------- ");
			ardrone.takeOff();
		}
		
		switch (note) {
			case 43:
				System.out.println("GOING LEFT!!!");
				ardrone.spinLeft(40); // move forward
				//ardrone.down();
				break;
			case 55:
				System.out.println("SPIN RIGHT!!!");
				ardrone.spinRight(40); // move forward
				break;
			case 59:
				System.out.println("GOING DOWN!!!");
				ardrone.spinRight(40); // move backwards
				//ardrone.up();
				break;
			case 47:
				System.out.println("GOING LEFT!!!");
				ardrone.goLeft(40);
				break;
			case 77:
				System.out.println("GOING RIGHT!!!");
				ardrone.goRight(40);
				break;
			case 70:
				System.out.println("GOING FORWARDS!!!");
				ardrone.forward(40);
				break;
			case 71:
				System.out.println("GOING BACKWARDS!!!");
				ardrone.backward(40);
				break;
			case 65:
				System.out.println("GOING LEFT!!!");
				ardrone.spinLeft(40); // move forward
				//ardrone.down();
				break;
			case 37:
				System.out.println("SPIN RIGHT!!!");
				ardrone.spinRight(40); // move forward
				break;
			case 54:
				System.out.println("GOING DOWN!!!");
				ardrone.down(40); // move backwards
				//ardrone.up();
				break;
			case 63:
				System.out.println("GOING LEFT!!!");
				ardrone.goLeft(40);
				break;
			case 44:
				System.out.println("GOING RIGHT!!!");
				ardrone.goRight(40);
				break;
			case 48:
				System.out.println("GOING FORWARDS!!!");
				ardrone.forward(40);
				break;
			case 42:
				System.out.println("GOING BACKWARDS!!!");
				ardrone.backward(40);
				break;
			case 38:
				System.out.println("GOING LEFT!!!");
				ardrone.spinLeft(40); // move forward
				//ardrone.down();
				break;
			case 25:
				System.out.println("SPIN RIGHT!!!");
				ardrone.spinRight(40); // move forward
				break;
			case 50:
				System.out.println("GOING DOWN!!!");
				ardrone.spinRight(40); // move backwards
				//ardrone.up();
				break;
			case 67:
				System.out.println("GOING LEFT!!!");
				ardrone.goLeft(40);
				break;
			case 45:
				System.out.println("GOING RIGHT!!!");
				ardrone.goRight(40);
				break;
			case 68:
				System.out.println("GOING FORWARDS!!!");
				ardrone.forward(40);
				break;
			case 73:
				System.out.println("GOING BACKWARDS!!!");
				ardrone.backward(40);
				break;
			case 74:
				System.out.println("GOING LEFT!!!");
				ardrone.spinLeft(40); // move forward
				//ardrone.down();
				break;
			case 51:
				System.out.println("SPIN RIGHT!!!");
				ardrone.spinRight(40); // move forward
				break;
			case 52:
				System.out.println("GOING DOWN!!!");
				ardrone.down(40); // move backwards
				//ardrone.up();
				break;
			case 62:
				System.out.println("GOING LEFT!!!");
				ardrone.goLeft(40);
				break;
			case 66:
				System.out.println("GOING RIGHT!!!");
				ardrone.goRight(40);
				break;
			case 64:
				System.out.println("GOING FORWARDS!!!");
				ardrone.forward(40);
				break;
			case 39:
				System.out.println("GOING BACKWARDS!!!");
				ardrone.backward(40);
				break;
			case 49:
				System.out.println("GOING RIGHT!!!");
				ardrone.goRight(40);
				break;
			case 56:
				System.out.println("GOING FORWARDS!!!");
				ardrone.forward(40);
				break;
			case 46:
				System.out.println("GOING BACKWARDS!!!");
				ardrone.backward(40);
				break;
			case 76:
				System.out.println("GOING LEFT!!!");
				ardrone.spinLeft(40); // move forward
				//ardrone.down();
				break;
			case 75:
				System.out.println("SPIN RIGHT!!!");
				ardrone.spinRight(40); // move forward
				break;
			case 57:
				System.out.println("GOING DOWN!!!");
				ardrone.down(40); // move backwards
				//ardrone.up();
				break;
			case 53:
				System.out.println("GOING LEFT!!!");
				ardrone.goLeft(40);
				break;
			/**
			case 61:
				ardrone.move3D(0, 0, 0, 10); // move right
				//ardrone.goLeft();
				break;
			case 58:
				ardrone.move3D(0, 0, 0, -10); //move left
				//ardrone.goRight();
				break;
				**/
			case 36:
				ardrone.landing();
				break;
			default:
				break;
		}
		//ardrone.stop();
		return true;
	}
	public boolean velocityListener(int velocity){
		System.out.println("Velocity: " + velocity);
		return true;
	}
	
	private JPanel getMyPanel() {
		if (myPanel == null) {
			myPanel = new MyPanel();
		}
		return myPanel;
	}

	private class MyPanel extends JPanel {
		private static final long serialVersionUID = -7635284252404123776L;

		/** ardrone video image */
		private BufferedImage image = null;

		public void setImage(BufferedImage image) {
			this.image = image;
		}

		public void paint(Graphics g) {
			g.setColor(Color.white);
			g.fillRect(0, 0, this.getWidth(), this.getHeight());
			if (image != null)
				g.drawImage(image, 0, 0, image.getWidth(), image.getHeight(),
						null);
		}
	}
}
