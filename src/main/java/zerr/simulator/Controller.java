package zerr.simulator;

import java.util.concurrent.ArrayBlockingQueue;

import lombok.Builder;
import lombok.Data;
import zerr.configuration.model.ControllerConfModel;

@Data
@Builder
public final class Controller {
	private Integer bufferRequestSize;
	private ArrayBlockingQueue<Channel> queueRequest;
	
	public static Controller create(ControllerConfModel controller) {
		return Controller.builder()
				.bufferRequestSize(controller.getBufferRequestSize())
				.queueRequest(new ArrayBlockingQueue<>(controller.getBufferRequestSize()))
				.build();
	}
}
