package com.scs.splitscreenfpsengine.input;

import com.jme3.math.Matrix3f;
import com.jme3.math.Quaternion;
import com.jme3.math.Vector3f;
import com.jme3.renderer.Camera;
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
	private boolean[] abilityDisabled = new boolean[2];
	
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
			//Settings.p("Bwd:" + f);
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
		try {
			return c.isButtonPressed(ControllerButton.A);
		} catch (ControllerUnpluggedException e) {
			e.printStackTrace();
		}
		return false;
	}


	@Override
	public boolean isAbilityPressed(int num) {
			switch (num) {
			case 0:
				try {
					float f = c.getAxisState(ControllerAxis.TRIGGERRIGHT) - initialStates.states.get(ControllerAxis.TRIGGERRIGHT);
					if (f > getDeadzone()) {
						return !abilityDisabled[num];
					}
				} catch (ControllerUnpluggedException e) {
					e.printStackTrace();
				}
				abilityDisabled[num] = false;
				return false;
				
			case 1:
				try {
					float f = c.getAxisState(ControllerAxis.TRIGGERLEFT) - initialStates.states.get(ControllerAxis.TRIGGERLEFT);
					if (f > getDeadzone()) {
						return !abilityDisabled[num];
					}
				} catch (ControllerUnpluggedException e) {
					e.printStackTrace();
				}
				abilityDisabled[num] = false;
				return false;
			}
		return false;
	}


	@Override
	public void resetAbilitySwitch(int num) {
		abilityDisabled[num] = true;
	}


	@Override
	public void process(float tpfSecs) {
		try {
			{
				float f = c.getAxisState(ControllerAxis.RIGHTX) - initialStates.states.get(ControllerAxis.RIGHTX);
				if (Math.abs(f) > getDeadzone()) {
					this.rotateCamera(-f, tpfSecs, initialUpVec);
				}
			}
			{
				float f2 = c.getAxisState(ControllerAxis.RIGHTY) - initialStates.states.get(ControllerAxis.RIGHTY);
				if (Math.abs(f2) > getDeadzone()) {
					this.rotateCamera(-f2, tpfSecs, cam.getLeft());
				}
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
	protected void rotateCamera(float value, float tpfSecs, Vector3f axis){
		//float adjustedValue = (float)Math.sqrt(1 * (double)value * (double)tpfSecs);
		float adjustedValue = 2 * value * tpfSecs;
		Matrix3f mat = new Matrix3f();
		mat.fromAngleNormalAxis(adjustedValue, axis);

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
		try {
			if (fwd) {
				return c.isButtonPressed(ControllerButton.LEFTBUMPER);
			} else {
				return c.isButtonPressed(ControllerButton.RIGHTBUMPER);
			}
		} catch (ControllerUnpluggedException e) {
			e.printStackTrace();
		}
		return false;
	}
}
