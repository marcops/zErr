package zerr.simulator.hardware.memory;

import java.util.concurrent.ArrayBlockingQueue;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class ChannelBuffer {
	private ArrayBlockingQueue<ChannelEvent> in;
	private ArrayBlockingQueue<ChannelEvent> out;

	public static ChannelBuffer create(Integer bufferSize) {
		return ChannelBuffer.builder()
				.in(new ArrayBlockingQueue<>(bufferSize - 1))
				.out(new ArrayBlockingQueue<>(1))
				.build();
	}
}
