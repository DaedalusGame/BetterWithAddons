package betterwithaddons;

public interface IProxy
{
	void preInit();

	void init();

	void postInit();

	void makeLeafFX(double x, double y, double z, float r, float g, float b, float size, float motionx, float motiony, float motionz, float maxAgeMul);

	void makeLightningFX(double x, double y, double z, float r, float g, float b, float size, float maxAgeMul);

	void registerResourcePack();

	void addResourceOverride(String space, String dir, String file, String ext);

	void addResourceOverride(String modid, String space, String dir, String file, String ext);
}
