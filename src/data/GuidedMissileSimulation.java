package data;
class PIDController {
	private double kp;
	private double ki;
	private double kd;

	private double integral;
	private double previousError;

	public PIDController(double kp, double ki, double kd) {
		this.kp = kp;
		this.ki = ki;
		this.kd = kd;
		this.integral = 0.0;
		this.previousError = 0.0;
	}

	public double calculate(double target, double current, double deltaTime) {
		double error = target - current;

		// 비례 항
		double proportional = kp * error;

		// 적분 항
		integral += error * deltaTime;

		// 미분 항
		double derivative = (error - previousError) / deltaTime;

		// PID 계산
		double output = proportional + (ki * integral) + (kd * derivative);

		// 이전 오차 업데이트
		previousError = error;

		return output;
	}
}

public class GuidedMissileSimulation {
//	// 초기 미사일 좌표
//	double missileX;
//	double missileY;
//
//	// 목표 좌표 (상대방 위치)
//	double targetX ;
//	double targetY;
	double deltaTime;
	PIDController pidX; // X축 PID
	PIDController pidY; // Y축 PID	
	
	public GuidedMissileSimulation() {
		
//		this.missileX = missileX;
//		this.missileY = missileY;

//		this.targetX = targetX;
//		this.targetY = targetY;		
		// PID 컨트롤러 생성 (비례, 적분, 미분 상수 조정)
//		pidX = new PIDController(0.5, 0.01, 0.1); // X축 PID
//		pidY = new PIDController(0.5, 0.01, 0.1); // Y축 PID		

		deltaTime = 1; // 초 단위
//		// 시뮬레이션 루프
//		for (int i = 0; i < 200; i++) { // 200번 반복
//			// X축과 Y축 각각의 제어 신호 계산
//			double controlX = pidX.calculate(targetX, missileX, deltaTime);
//			double controlY = pidY.calculate(targetY, missileY, deltaTime);
//
//			// 미사일 위치 업데이트
//			missileX += controlX * deltaTime;
//			missileY += controlY * deltaTime;
//
//
//			// 목표에 도달했는지 확인
//			if (Math.abs(missileX - targetX) < 0.1 && Math.abs(missileY - targetY) < 0.1) {
//				System.out.println("Missile reached the target!");
//				break;
//			}
//		}

	}
	public double getX(double missileX,double targetX) {		
		pidX = new PIDController(0.5, 0.01, 0.1); // X축 PID
		double controlX = pidX.calculate(targetX, missileX, deltaTime);
		missileX += controlX * deltaTime;
		return missileX;
	}
	public double getY(double missileY,double targetY) {
		
		pidY = new PIDController(0.5, 0.01, 0.1); // Y축 PID		
		double controlY = pidY.calculate(targetY, missileY, deltaTime);
		missileY += controlY * deltaTime;
		return missileY;
	}
	
}
