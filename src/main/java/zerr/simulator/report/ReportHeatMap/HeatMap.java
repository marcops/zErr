package zerr.simulator.report.ReportHeatMap;

import java.util.HashMap;

import com.fasterxml.jackson.annotation.JsonIgnore;

import lombok.Data;
import zerr.simulator.hardware.memcontroller.PhysicalAddress;

/*
 * Todo pensar em algo que tenha um incremento e ainda dê a %
 * exemplo recebeu acesso vai de 1000 para 1001
 * recebeu erro vai de 1000 para 2000 (como se fosse %, mas sem ter o total de cada 1)
 * */
//PEnsar em limites etc.
//ao inves de ter 4 campos, ter 2 ou 1.
@Data
public abstract class HeatMap<T extends IHeatMap> implements IHeatMap {
	protected HashMap<Integer, T> map = new HashMap<>();
	protected long hardError;
	protected long softError;
//	protected long writeAccess;
//	protected long readAccess;

	@JsonIgnore
	public abstract T getNew();
	@JsonIgnore
	public abstract long getPosition(PhysicalAddress phy);
	@JsonIgnore
	public abstract boolean useMap();

	
//	@Override
//	public void addReadAccess(PhysicalAddress phy, long qtd) {
//		readAccess+=qtd;
//		if (useMap()) {
//			if(!map.containsKey((int)getPosition(phy))) map.put((int)getPosition(phy), getNew());
//			map.get((int)getPosition(phy)).addReadAccess(phy, qtd);
//		}
//	}
//
//	@Override
//	public void addWriteAccess(PhysicalAddress phy, long qtd) {
//		writeAccess++;
//		if (useMap()) {
//			if(!map.containsKey((int)getPosition(phy))) map.put((int)getPosition(phy), getNew());
//			map.get((int)getPosition(phy)).addWriteAccess(phy, qtd);
//		}
//	}

	@Override
	public void addSoftError(PhysicalAddress phy, long qtd) {
		softError+=qtd;
		if (useMap()) {
			if(!map.containsKey((int)getPosition(phy))) map.put((int)getPosition(phy), getNew());
			map.get((int)getPosition(phy)).addSoftError(phy, qtd);
		}
	}

	@Override
	public void addHardError(PhysicalAddress phy, long qtd) {
		hardError+=qtd;
		if (useMap()) {
			if(!map.containsKey((int)getPosition(phy))) map.put((int)getPosition(phy), getNew());
			map.get((int)getPosition(phy)).addHardError(phy,qtd);
		}
	}
	
//	public /* synchronized void */ long getStatistic() {
//		// TODO AQUI PENSAR EM UMA ESTATISTICA LEGAL
//		long base = writeAccess + readAccess;
//		if (base <= 0) return 0;
//		return (softError + hardError) * 100 / base;
//	}

}
