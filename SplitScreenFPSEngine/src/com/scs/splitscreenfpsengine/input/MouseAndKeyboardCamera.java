package com.scs.splitscreenfpsengine.input;

import com.jme3.input.InputManager;
import com.jme3.input.KeyInput;
import com.jme3.input.MouseInput;
import com.jme3.input.controls.ActionListener;
import com.jme3.input.controls.AnalogListener;
import com.jme3.input.controls.KeyTrigger;
import com.jme3.input.controls.MouseAxisTrigger;
import com.jme3.input.controls.MouseButtonTrigger;
import com.jme3.math.Matrix3f;
import com.jme3.math.Quaternion;
import com.jme3.math.Vector3f;
import com.jme3.renderer.Camera;
import com.scs.splitscreenfpsengine.Settings;

public class MouseAndKeyboardCamera implements AnalogListener, ActionListener, IInputDevice { 

	protected float rotationSpeed = 10f;

	private boolean left = false, right = false, up = false, down = false, jump = false;
	private boolean ability0 = false, ability1 = false;
	private boolean cycleFwd = false, cycleBwd = false;

	private InputManager inputManager;
	private Camera cam;
	private Vector3f initialUpVec; 


	public MouseAndKeyboardCamera(Camera _cam, InputManager _inputManager) {
		//super(cam);
		
		cam = _cam;
		this.inputManager = _inputManager;
        initialUpVec = cam.getUp().clone();
        inputManager.setCursorVisible(false);
        
		inputManager.addMapping("Left", new KeyTrigger(KeyInput.KEY_A));
		inputManager.addListener(this, "Left");
		inputManager.addMapping("Right", new KeyTrigger(KeyInput.KEY_D));
		inputManager.addListener(this, "Right");
		inputManager.addMapping("Up", new KeyTrigger(KeyInput.KEY_W));
		inputManager.addListener(this, "Up");
		inputManager.addMapping("Down", new KeyTrigger(KeyInput.KEY_S));
		inputManager.addListener(this, "Down");
		inputManager.addMapping("Jump", new KeyTrigger(KeyInput.KEY_SPACE));
		inputManager.addListener(this, "Jump");
		inputManager.addMapping("Shoot", new MouseButtonTrigger(MouseInput.BUTTON_LEFT));
		inputManager.addListener(this, "Shoot");
		inputManager.addMapping("Ability1", new MouseButtonTrigger(MouseInput.BUTTON_RIGHT));
		inputManager.addListener(this, "Ability1");

		// both mouse and button - rotation of cam
		inputManager.addMapping("mFLYCAM_Left", new MouseAxisTrigger(MouseInput.AXIS_X, true), new KeyTrigger(KeyInput.KEY_LEFT));
		inputManager.addListener(this, "mFLYCAM_Left");

		inputManager.addMapping("mFLYCAM_Right", new MouseAxisTrigger(MouseInput.AXIS_X, false), new KeyTrigger(KeyInput.KEY_RIGHT));
		inputManager.addListener(this, "mFLYCAM_Right");

		inputManager.addMapping("mFLYCAM_Up", new MouseAxisTrigger(MouseInput.AXIS_Y, false), new KeyTrigger(KeyInput.KEY_UP));
		inputManager.addListener(this, "mFLYCAM_Up");

		inputManager.addMapping("mFLYCAM_Down", new MouseAxisTrigger(MouseInput.AXIS_Y, true), new KeyTrigger(KeyInput.KEY_DOWN));
		inputManager.addListener(this, "mFLYCAM_Down");

		// mouse only - zoom in/out with wheel, and rotate drag
		inputManager.addMapping("FLYCAM_ZoomIn", new MouseAxisTrigger(MouseInput.AXIS_WHEEL, false));
		inputManager.addListener(this, "FLYCAM_ZoomIn");
		inputManager.addMapping("FLYCAM_ZoomOut", new MouseAxisTrigger(MouseInput.AXIS_WHEEL, true));
		inputManager.addListener(this, "FLYCAM_ZoomOut");
		//inputManager.addMapping("FLYCAM_RotateDrag", new MouseButtonTrigger(MouseInput.BUTTON_LEFT));*/

		// keyboard only WASD for movement and WZ for rise/lower height
		/*inputManager.addMapping("FLYCAM_StrafeLeft", new KeyTrigger(KeyInput.KEY_A));
		inputManager.addMapping("FLYCAM_StrafeRight", new KeyTrigger(KeyInput.KEY_D));
		inputManager.addMapping("FLYCAM_Forward", new KeyTrigger(KeyInput.KEY_W));
		inputManager.addMapping("FLYCAM_Backward", new KeyTrigger(KeyInput.KEY_S));
		inputManager.addMapping("FLYCAM_Rise", new KeyTrigger(KeyInput.KEY_Q));
		inputManager.addMapping("FLYCAM_Lower", new KeyTrigger(KeyInput.KEY_Z));*/

		//inputManager.addListener(this, mappings);  scs!
		//inputManager.setCursorVisible(dragToRotate || !isEnabled());

		/*Joystick[] joysticks = inputManager.getJoysticks();
        if (joysticks != null && joysticks.length > 0){
            for (Joystick j : joysticks) {
                mapJoystick(j);
            }
        }*/
	}


	@Override
	public void onAnalog(String name, float value, float tpf) {
		//Settings.p("name=" + name + ", value=" + value);

		if (Settings.DEBUG_ROTATING_CAM) {
			Settings.p("name=" + name + ", value=" + value);
			//Settings.p("CAM=" +this.cam.getName());
		}

		if (name.equals("mFLYCAM_Left")){
			//Settings.p("name=" + name);
			rotateCamera(value, initialUpVec);
		}else if (name.equals("mFLYCAM_Right")){
			rotateCamera(-value, initialUpVec);
		}else if (name.equals("mFLYCAM_Up")){
			rotateCamera(-value, cam.getLeft());
		}else if (name.equals("mFLYCAM_Down")){
			rotateCamera(value, cam.getLeft());
		}/*else if (name.equals("FLYCAM_Forward")){
			moveCamera(value, false);
		}else if (name.equals("FLYCAM_Backward")){
			moveCamera(-value, false);
		}else if (name.equals("FLYCAM_StrafeLeft")){
			moveCamera(value, true);
		}else if (name.equals("FLYCAM_StrafeRight")){
			moveCamera(-value, true);
		}else if (name.equals("FLYCAM_Rise")){
			riseCamera(value);
		}else if (name.equals("FLYCAM_Lower")){
			riseCamera(-value);
		}*/ else if (name.equals("FLYCAM_ZoomIn")){
			//zoomCamera(value);
			cycleFwd = true;
			cycleBwd = false;
		} else if (name.equals("FLYCAM_ZoomOut")){
			cycleFwd = false;
			cycleBwd = true;
		}
	}


	/**
	 * Rotate the camera by the specified amount around the specified axis.
	 *
	 * @param value rotation amount
	 * @param axis direction of rotation (a unit vector)
	 */
	protected void rotateCamera(float value, Vector3f axis){
		Matrix3f mat = new Matrix3f();
		mat.fromAngleNormalAxis(rotationSpeed * value, axis);

		Vector3f up = cam.getUp();
		Vector3f left = cam.getLeft();
		Vector3f dir = cam.getDirection();

		mat.mult(up, up);
		mat.mult(left, left);
		mat.mult(dir, dir);

		Quaternion q = new Quaternion();
		q.fromAxes(left, up, dir);
		q.normalizeLocal();

		cam.setAxes(q);
	}

	
	public void onAction(String binding, boolean isPressed, float tpf) {
		if (binding.equals("Left")) {
			left = isPressed;
		} else if (binding.equals("Right")) {
			right = isPressed;
		} else if (binding.equals("Up")) {
			up = isPressed;
		} else if (binding.equals("Down")) {
			down = isPressed;
		} else if (binding.equals("Jump")) {
			jump = isPressed;
		} else if (binding.equals("Shoot")) {
			ability0 = isPressed;
		} else if (binding.equals("Ability1")) {
			ability1 = isPressed;
		}		
	}


	@Override
	public float getFwdValue() {
		return up ? 1f : 0f;//this.fwdVal;
	}


	@Override
	public float getBackValue() {
		return down ? 1f : 0f;
	}


	@Override
	public float getStrafeLeftValue() {
		return left ? 1f : 0f;
	}


	@Override
	public float getStrafeRightValue() {
		return right ? 1f : 0f;
	}        


	@Override
	public boolean isJumpPressed() {
		return jump;
	}


	@Override
	public boolean isAbilityPressed(int num) {
		if (num == 0) {
			return ability0;
		} else if (num == 1) {
			return ability1;
		} else {
			throw new RuntimeException("Todo");
		}
	}


	@Override
	public void resetAbilitySwitch(int num) {
		if (num == 0) {
			ability0 = false;
		} else if (num == 1) {
			ability1 = false;
		} else {
			throw new RuntimeException("Todo");
		}
	}


	@Override
	public void process(float tpfSecs) {
		// Do nothing

	}


	@Override
	public boolean isCycleAbilityPressed(boolean fwd) {
		if (fwd && this.cycleFwd) {
			cycleFwd = false;
			return true;
		} else if (!fwd && this.cycleBwd) {
			cycleBwd = false;
			return true;
		}
		return false;
	}

}
