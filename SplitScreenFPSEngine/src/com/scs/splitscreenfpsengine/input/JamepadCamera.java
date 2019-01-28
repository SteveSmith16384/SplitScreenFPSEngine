package com.scs.splitscreenfpsengine.input;

import com.jme3.math.Matrix3f;
import com.jme3.math.Quaternion;
import com.jme3.math.Vector3f;
import com.jme3.renderer.Camera;
import com.scs.splitscreenfpsengine.Settings;
import com.scs.splitscreenfpsengine.jamepad.JamepadFullAxisState;
import com.studiohartman.jamepad.ControllerAxis;
import com.studiohartman.jamepad.ControllerButton;
import com.studiohartman.jamepad.ControllerIndex;
import com.studiohartman.jamepad.ControllerUnpluggedException;

public class JamepadCamera implements IInputDevice {  

	private static final float DEADZONE = 0.1f;

	private Camera cam;
	private ControllerIndex c;
	private JamepadFullAxisState initialStates;

	private Vector3f initialUpVec;
	protected float rotationSpeed = .1f;

	public JamepadCamera(Camera _cam, ControllerIndex _c, JamepadFullAxisState _states) {
		cam = _cam;
		c = _c;
		initialStates = _states;

		initialUpVec = cam.getUp().clone();

	}


	private float getDeadzone() {
		return DEADZONE;
	}


	@Override
	public float getFwdValue() {
		try {
			float f = c.getAxisState(ControllerAxis.LEFTY) - initialStates.states.get(ControllerAxis.LEFTY);
			if (f > getDeadzone()) {
				return f;
			}
		} catch (ControllerUnpluggedException e) {
			e.printStackTrace();
		}
		return 0;
	}

	@Override
	public float getBackValue() {
		try {
			float f = -1 * c.getAxisState(ControllerAxis.LEFTY) - initialStates.states.get(ControllerAxis.LEFTY);
			Settings.p("Bwd:" + f);
			if (f > getDeadzone()) {
				return f;
			}
		} catch (ControllerUnpluggedException e) {
			e.printStackTrace();
		}
		return 0;
	}

	@Override
	public float getStrafeLeftValue() {
		try {
			float f = -1 * c.getAxisState(ControllerAxis.LEFTX) - initialStates.states.get(ControllerAxis.LEFTX);
			if (f > getDeadzone()) {
				return f;
			}
		} catch (ControllerUnpluggedException e) {
			e.printStackTrace();
		}
		return 0;
	}

	@Override
	public float getStrafeRightValue() {
		try {
			float f = c.getAxisState(ControllerAxis.LEFTX) - initialStates.states.get(ControllerAxis.LEFTX);
			if (f > getDeadzone()) {
				return f;
			}
		} catch (ControllerUnpluggedException e) {
			e.printStackTrace();
		}
		return 0;
	}


	@Override
	public boolean isJumpPressed() {
		// TODO Auto-generated method stub
		return false;
	}


	@Override
	public boolean isAbilityPressed(int num) {
		try {
			switch (num) {
			case 0:
				if (c.isButtonPressed(ControllerButton.X)) {
					return true;
				}
				break;
			case 1:
				if (c.isButtonPressed(ControllerButton.Y)) {
					return true;
				}
				break;
			}
		} catch (ControllerUnpluggedException e) {
			e.printStackTrace();
		}
		return false;
	}


	@Override
	public void resetAbilitySwitch(int num) {
		// TODO Auto-generated method stub

	}


	@Override
	public void process(float tpfSecs) {
		try {
			{ // todo - check deadzone!
				float f = c.getAxisState(ControllerAxis.RIGHTX) - initialStates.states.get(ControllerAxis.RIGHTX);
				this.rotateCamera(-f * tpfSecs, initialUpVec);
			}
			{
				float f2 = c.getAxisState(ControllerAxis.RIGHTY) - initialStates.states.get(ControllerAxis.RIGHTY);
				this.rotateCamera(-f2 * tpfSecs, cam.getLeft());
			}
		} catch (ControllerUnpluggedException e) {
			e.printStackTrace();
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
		mat.fromAngleNormalAxis(10f * value, axis);

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


	@Override
	public boolean isCycleAbilityPressed(boolean fwd) {
		// todo
		return false;

	}
}
