package zerr.simulator.faultinjection;

public enum FaultMode {
	RANDOM(new RandomFaultMode()),
	AROUND(new AroundFaultMode());
	
	private final IFaultMode type;

	FaultMode(final IFaultMode type) {
        this.type = type;
    }
	
	public IFaultMode getType() {
		return type;
	}

}
