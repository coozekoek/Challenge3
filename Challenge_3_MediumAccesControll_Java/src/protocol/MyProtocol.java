package protocol;

import java.util.Random;

/**
 * A fairly trivial Medium Access Control scheme.
 * @author Jaco ter Braak, Twente University
 * @version 05-12-2013
 */
public class MyProtocol implements IMACProtocol {
	
	int timeOut = 0;
	//use full reach of the integer values to minimize the chance for same controlInfo for 2 clients
	int myControl = new Random().nextInt((int) Math.pow(2, 32));
	int previousQueueLength;
	
	@Override
	public TransmissionInfo TimeslotAvailable(MediumState previousMediumState,
			int controlInformation, int localQueueLength) {
		// No data to send, just be quiet
		if (localQueueLength == 0) {
			System.out.println("SLOT - No data to send.");
			return new TransmissionInfo(TransmissionType.Silent, myControl);
		}
		
		//check if timeOut is over
		if (timeOut != 0) {
			timeOut--;
			return new TransmissionInfo(TransmissionType.Silent, myControl);
		}
		
		//if channel is free try to send control int
		if (previousMediumState == MediumState.Idle) {
			previousQueueLength = localQueueLength;
			System.out.println("SLOT - Sending RTS and hope for no collision.");
			return new TransmissionInfo(TransmissionType.NoData, myControl);
		//if previous transmit was succesful check the controllInfo
		} else if (previousMediumState == MediumState.Succes) {
			//if controlInformation is myControl, treat as CTS
			if (controlInformation == myControl && (previousQueueLength - localQueueLength) < 5) {
				System.out.println("SLOT - Sending data");
				return new TransmissionInfo(TransmissionType.Data, myControl);
			//if controlInformation is not myControl, treat as NCTS (not clear to send)
			} else {
				System.out.println("SLOT - Staying quiet");
				return new TransmissionInfo(TransmissionType.Silent, myControl);
			}
		//if collision wait a random amount of timeslots
		} else {
			timeOut = new Random().nextInt(4);
			System.out.println("SLOT - Collision detected, so timeout for " + timeOut + " slots");
			return new TransmissionInfo(TransmissionType.Silent, myControl);
		}
//		if (previousMediumState == MediumState.Collision) {
//			timeOut = new Random().nextInt(4);
//			return new TransmissionInfo(TransmissionType.Silent, myControl);
//		} else {
//			timeOut--;
//			return new TransmissionInfo(TransmissionType.Silent, myControl);
//		}
//		

	}

}
