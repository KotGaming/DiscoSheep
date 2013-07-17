package gibstick.bukkit.discosheep;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import org.bukkit.Color;
import org.bukkit.DyeColor;
import org.bukkit.Location;
import org.bukkit.Sound;
import org.bukkit.World;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Firework;
import org.bukkit.entity.Player;
import org.bukkit.entity.Sheep;
import org.bukkit.FireworkEffect;
import org.bukkit.FireworkEffect.Builder;
import org.bukkit.inventory.meta.FireworkMeta;
import org.bukkit.util.Vector;

/**
 * 
 * @author Georgiy
 */
public class DiscoParty
{

	private DiscoSheep ds;
	private Player player;
	private ArrayList<Sheep> sheepList = new ArrayList<Sheep>();
	private int duration, frequency = 20;
	static int defaultDuration = 300; // ticks for entire party
	static int defaultPeriod = 10; // ticks per state change
	static int defaultRadius = 5;
	static int defaultSheep = 10;
	static float defaultSheepJump = 0.5f;
	static boolean defaultPartyOnJoin = false;
	static int maxDuration = 2400; // 120 seconds
	static int maxSheep = 100;
	static int maxRadius = 100;
	static int minPeriod = 5; // 0.25 seconds
	static int maxPeriod = 40; // 2.0 seconds
	private boolean doFireworks = false;
	private boolean doJump = true;
	private int state = 0;
	private DiscoUpdater updater;
	private static final DyeColor[] discoColours =
	{
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
	};

	public DiscoParty(DiscoSheep parent, Player player)
	{
		this.ds = parent;
		this.player = player;
	}

	List<Sheep> getSheep()
	{
		return sheepList;
	}

	void spawnSheep(World world, Location loc)
	{
		Sheep newSheep = (Sheep) world.spawnEntity(loc, EntityType.SHEEP);
		newSheep.setColor(discoColours[(int) (Math.random() * (discoColours.length - 1))]);
		newSheep.setBreed(false);
		getSheep().add(newSheep);
	}

	// Spawn some number of sheep next to given player
	void spawnSheep(int num, int sheepSpawnRadius)
	{
		Location loc;
		World world = player.getWorld();

		for (int i = 0; i < num; i++)
		{
			double x = player.getLocation().getX();
			double z = player.getLocation().getZ();
			double y;

			// random point on circle with polar coordinates
			double r = Math.sqrt(Math.random()) * sheepSpawnRadius; // sqrt for
																	// uniform
																	// distribution
			double azimuth = Math.random() * 2 * Math.PI; // radians
			x += r * Math.cos(azimuth);
			z += r * Math.sin(azimuth);

			y = world.getHighestBlockYAt((int) x, (int) z);
			loc = new Location(world, x, y, z);
			// loc.setYaw(0);
			// loc.setPitch((float) Math.random() * 360 - 180);
			spawnSheep(world, loc);
		}
	}

	// Mark all sheep in the sheep array for removal, then clear the array
	void removeAllSheep()
	{
		for (Sheep sheep : getSheep())
		{
			sheep.setHealth(0);
			sheep.remove();
		}
		getSheep().clear();
	}

	// Set a random colour for all sheep in array
	void randomizeSheepColour(Sheep sheep)
	{
		sheep.setColor(discoColours[(int) Math.round(Math.random() * (discoColours.length - 1))]);
	}

	void jumpSheep(Sheep sheep)
	{
		Vector orgVel = sheep.getVelocity();
		Vector newVel = (new Vector()).copy(orgVel);
		newVel.add(new Vector(0, defaultSheepJump, 0));
		sheep.setVelocity(newVel);
	}

	private Color getColor(int i)
	{
		Color c = null;
		if (i == 1)
		{
			c = Color.AQUA;
		}
		if (i == 2)
		{
			c = Color.BLACK;
		}
		if (i == 3)
		{
			c = Color.BLUE;
		}
		if (i == 4)
		{
			c = Color.FUCHSIA;
		}
		if (i == 5)
		{
			c = Color.GRAY;
		}
		if (i == 6)
		{
			c = Color.GREEN;
		}
		if (i == 7)
		{
			c = Color.LIME;
		}
		if (i == 8)
		{
			c = Color.MAROON;
		}
		if (i == 9)
		{
			c = Color.NAVY;
		}
		if (i == 10)
		{
			c = Color.OLIVE;
		}
		if (i == 11)
		{
			c = Color.ORANGE;
		}
		if (i == 12)
		{
			c = Color.PURPLE;
		}
		if (i == 13)
		{
			c = Color.RED;
		}
		if (i == 14)
		{
			c = Color.SILVER;
		}
		if (i == 15)
		{
			c = Color.TEAL;
		}
		if (i == 16)
		{
			c = Color.WHITE;
		}
		if (i == 17)
		{
			c = Color.YELLOW;
		}

		return c;
	}

	void updateAllSheep()
	{
		@SuppressWarnings("unused")
		int i = 0;
		for (Sheep sheep : getSheep())
		{
			randomizeSheepColour(sheep);
			if (doFireworks && state % 8 == 0)
			{
				spawnRandomFireworkAtSheep(sheep);
			}
			if (doJump)
			{
				if (state % 2 == 0)
				{
					if (Math.random() < 0.5)
					{
						jumpSheep(sheep);
					}
				}
			}
			i++;
		}
	}

	void playSounds()
	{
		player.playSound(player.getLocation(), Sound.NOTE_BASS_DRUM, 1.0f, 1.0f);
		if (this.state % 2 == 0)
		{
			player.playSound(player.getLocation(), Sound.NOTE_SNARE_DRUM, 1.0f, 1.0f);
		}
		if (this.state % 4 == 0)
		{
			player.playSound(player.getLocation(), Sound.NOTE_STICKS, 1.0f, 1.0f);
		}
		player.playSound(player.getLocation(), Sound.BURP, 0.5f, (float) Math.random() + 1);
	}

	void randomizeFirework(Firework firework)
	{
		Random r = new Random();
		Builder effect = FireworkEffect.builder();
		FireworkMeta meta = firework.getFireworkMeta();

		// construct [1, 3] random colours
		int numColours = r.nextInt(3) + 1;
		Color[] colourArray = new Color[numColours];
		for (int i = 0; i < numColours; i++)
		{
			colourArray[i] = getColor(r.nextInt(17) + 1);
		}

		// randomize effects
		effect.withColor(colourArray);
		effect.flicker(r.nextDouble() < 0.5);
		effect.trail(r.nextDouble() < 0.5);
		effect.with(FireworkEffect.Type.values()[r.nextInt(FireworkEffect.Type.values().length)]);

		// set random effect and randomize power
		meta.addEffect(effect.build());
		meta.setPower(r.nextInt(2));

		// apply it to the given firework
		firework.setFireworkMeta(meta);
	}

	void spawnRandomFireworkAtSheep(Sheep sheep)
	{
		Firework firework = (Firework) sheep.getWorld().spawnEntity(sheep.getEyeLocation(),
				EntityType.FIREWORK);
		randomizeFirework(firework);
	}

	void update()
	{
		if (duration > 0)
		{
			updateAllSheep();
			playSounds();
			duration -= frequency;
			this.scheduleUpdate();
			this.state++;
		}
		else
		{
			this.stopDisco();
		}
	}

	void scheduleUpdate()
	{
		updater = new DiscoUpdater(this);
		updater.runTaskLater(ds, this.frequency);
	}

	void startDisco(int duration, int sheepAmount, int radius, int frequency, boolean fireworks)
	{
		if (this.duration > 0)
		{
			stopDisco();
		}
		this.doFireworks = fireworks;
		this.spawnSheep(sheepAmount, radius);
		this.frequency = frequency;
		this.duration = duration;
		this.scheduleUpdate();
		ds.getPartyMap().put(this.player.getName(), this);
	}

	void stopDisco()
	{
		removeAllSheep();
		this.duration = 0;
		if (updater != null)
		{
			updater.cancel();
		}
		updater = null;
		ds.getPartyMap().remove(this.player.getName());
	}
}
