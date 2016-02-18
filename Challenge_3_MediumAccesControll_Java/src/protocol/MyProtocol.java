package protocol;

import java.util.Random;

/**
 * A fairly trivial Medium Access Control scheme.
 * @author Jaco ter Braak, Twente University
 * @version 05-12-2013
 */
public class MyProtocol implements IMACProtocol {
	
	int timeOut = 0;
	
	@Override
	public TransmissionInfo TimeslotAvailable(MediumState previousMediumState,
			int controlInformation, int localQueueLength) {
		// No data to send, just be quiet
		if (localQueueLength == 0) {
			System.out.println("SLOT - No data to send." + controlInformation);
			return new TransmissionInfo(TransmissionType.Silent, 0);
		}

		if ((previousMediumState == MediumState.Idle || previousMediumState == MediumState.Succes) && timeOut == 0) {
			System.out.println("SLOT - Sending data and hope for no collision."  + controlInformation);
			return new TransmissionInfo(TransmissionType.Data, 0);
		} else if (previousMediumState == MediumState.Collision) {
			timeOut = new Random().nextInt(4);
			return new TransmissionInfo(TransmissionType.Silent, 0);
		} else {
			timeOut--;
			return new TransmissionInfo(TransmissionType.Silent, 0);
		}
		

	}

}
