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

	private Camera cam;
	private ControllerIndex c;
	private JamepadFullAxisState states;
	
	private Vector3f initialUpVec; 
	protected float rotationSpeed = 1f;

	public JamepadCamera(Camera _cam, ControllerIndex _c, JamepadFullAxisState _states) {
		cam = _cam;
		c = _c;
		states = _states;
		
        initialUpVec = cam.getUp().clone();

	}

	@Override
	public float getFwdValue() {
		try {
			float f = c.getAxisState(ControllerAxis.LEFTY);
			if (f > 0) {
				return f;
			}
		} catch (ControllerUnpluggedException e) {
			e.printStackTrace();
		}
		return 0;
	}

	@Override
	public float getBackValue() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public float getStrafeLeftValue() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public float getStrafeRightValue() {
		// TODO Auto-generated method stub
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
		/*
        if (name.equals(CameraInput.FLYCAM_LEFT)){
            rotateCamera(value, initialUpVec);
        }else if (name.equals(CameraInput.FLYCAM_RIGHT)){
            rotateCamera(-value, initialUpVec);
        }else if (name.equals(CameraInput.FLYCAM_UP)){
            rotateCamera(-value * (invertY ? -1 : 1), cam.getLeft());
        }else if (name.equals(CameraInput.FLYCAM_DOWN)){
            rotateCamera(value * (invertY ? -1 : 1), cam.getLeft());
        }*/

		try {
			float f = c.getAxisState(ControllerAxis.RIGHTX);
			this.rotateCamera(f, initialUpVec);
			
			float f2 = c.getAxisState(ControllerAxis.RIGHTY);
			this.rotateCamera(f2, cam.getLeft());
			
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

	
	@Override
	public boolean isCycleAbilityPressed(boolean fwd) {
		// todo
		return false;
		
	}
}
