package gibstick.bukkit.discosheep;

import java.util.ArrayList;
import java.util.List;
import org.bukkit.DyeColor;
import org.bukkit.Location;
import org.bukkit.Sound;
import org.bukkit.World;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.entity.Sheep;
import org.bukkit.plugin.java.JavaPlugin;

public final class DiscoSheep extends JavaPlugin {

	private ArrayList<Sheep> sheepArray = new ArrayList<Sheep>();
	private ArrayList<Player> playerArray = new ArrayList<Player>();
	private SheepDeshearer deshear = new SheepDeshearer(sheepArray);
	private static final DyeColor[] discoColours = {
		DyeColor.RED,
		DyeColor.ORANGE,
		DyeColor.YELLOW,
		DyeColor.GREEN,
		DyeColor.BLUE,
		DyeColor.LIGHT_BLUE,
		DyeColor.PINK,
		DyeColor.MAGENTA,
		DyeColor.LIME,
		DyeColor.CYAN,
		DyeColor.PURPLE
	}; // array of accetable disco colours (order not important)
	private DiscoUpdater updater;
	// radius for random sheep spawns around player
	private final int sheepSpawnRadius = 5;
	private final int defaultSheepAmount = 10;
	private final int defaultDuration = 300; // ticks for entire party
	private final int defaultFrequency = 10; // ticks per state change
	private int frequency = 0, duration = 0;

	@Override
	public void onEnable() {
		getCommand("ds").setExecutor(new DiscoSheepCommandExecutor(this));
		getServer().getPluginManager().registerEvents(deshear, this);
	}

	@Override
	public void onDisable() {
	}

	ArrayList<Sheep> getSheep() {
		return sheepArray;
	}

	ArrayList<Player> getPlayers() {
		return this.playerArray;
	}

	void spawnSheep(World world, Location loc) {
		Sheep newSheep = (Sheep) world.spawnEntity(loc, EntityType.SHEEP);
		newSheep.setMaxHealth(10000);
		newSheep.setHealth(10000);
		newSheep.setColor(discoColours[(int)Math.round(Math.random() * (discoColours.length - 1))]);
		getSheep().add(newSheep);
	}

	// Spawn some number of sheep next to given player
	void spawnSheep(Player player, int num) {
		Location loc;
		World world = player.getWorld();

		for (int i = 0; i < num; i++) {
			double x, y, z;

			// random x and z coordinates within a 5 block radius
			// safe y-coordinate
			x = -sheepSpawnRadius + (Math.random() * ((sheepSpawnRadius * 2) + 1)) + player.getLocation().getX();
			z = -sheepSpawnRadius + (Math.random() * ((sheepSpawnRadius * 2) + 1)) + player.getLocation().getZ();
			y = world.getHighestBlockYAt((int) x, (int) z);
			loc = new Location(world, x, y, z);
			spawnSheep(world, loc);
		}
	}

	// Mark all sheep in the sheep array for removal, then clear the array
	void removeAllSheep() {
		for (Sheep sheep : getSheep()) {
			sheep.setHealth(0);
			sheep.remove();
		}
		getSheep().clear();
	}

	// Set a random colour for all sheep in array
	void randomizeSheepColours() {
		for (Sheep sheep : getSheep()) {
			sheep.setColor(discoColours[(int)Math.round(Math.random() * (discoColours.length - 1))]);
		}
	}

	void playSounds() {
		for(Player player: getPlayers()){
			player.playSound(player.getLocation(), Sound.NOTE_BASS_DRUM, 1.0f, 1.0f);
                        player.playSound(player.getLocation(), Sound.BURP, frequency,(float) Math.random() + 1);
		}
	}

	void update() {
		if (duration > 0) {
			randomizeSheepColours();
			playSounds();
			duration -= frequency;
			this.scheduleUpdate();
		} else {
			this.stopDisco();
		}
	}

	void scheduleUpdate() {
		updater = new DiscoUpdater(this);
		updater.runTaskLater(this, this.frequency);
	}

	void startDisco(int duration, List<Player> players) {
		if (this.duration > 0) {
			stopDisco();
		}
		this.playerArray.addAll(players);
		for (Player player : players) {
			this.spawnSheep(player, this.defaultSheepAmount);
		}
		this.frequency = this.defaultFrequency;
		this.duration = this.defaultDuration;
		this.scheduleUpdate();
	}

	void startDisco(List<Player> players) {
		this.startDisco(this.defaultDuration, players);
	}

	void stopDisco() {
		removeAllSheep();
		this.playerArray.clear();
		this.duration = 0;
		if(updater != null)
			updater.cancel();
		updater = null;
	}
}
