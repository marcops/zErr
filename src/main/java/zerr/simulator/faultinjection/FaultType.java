package zerr.simulator.faultinjection;

public enum FaultType {
	RANDOM_TIME(new RandomTimeFaultType()),
	INSTRUCTION(new InstructionFaultType()),
	FIXED_TIME(null);
	
	private final IFaultType type;

	FaultType(final IFaultType type) {
        this.type = type;
    }
	
	public IFaultType getType() {
		return type;
	}

}
