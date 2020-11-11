package zerr.simulator.faultinjection;

public enum FaultMode {
	RANDOM_TIME(new RandomTimeFaultMode()),
	FIXED_TIME(null);
	
	private final IFaultMode mode;

	FaultMode(final IFaultMode mode) {
        this.mode = mode;
    }
	
	public IFaultMode getMode() {
		return mode;
	}

}
