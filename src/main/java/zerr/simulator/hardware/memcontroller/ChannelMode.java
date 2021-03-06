package zerr.simulator.hardware.memcontroller;

public enum ChannelMode {
	SINGLE(1), DUAL_CHANNEL(2), QUAD_CHANNEL(4);

	private final int value;

	ChannelMode(final int newValue) {
		value = newValue;
	}

	public int getValue() {
		return value;
	}

}
