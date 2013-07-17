/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package gibstick.bukkit.discosheep;

import org.bukkit.entity.Player;
import org.bukkit.entity.Sheep;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.event.player.PlayerShearEntityEvent;

/**
 * 
 * @author Mauve
 */
public class BaaBaaBlockSheepEvents implements Listener
{

	DiscoSheep parent;

	public BaaBaaBlockSheepEvents(DiscoSheep parent)
	{
		this.parent = parent;
	}

	// prevent sheep shearing
	@EventHandler
	public void onPlayerShear(PlayerShearEntityEvent e)
	{
		if (e.getEntity() instanceof Sheep)
		{
			for (DiscoParty party : parent.getParties())
			{
				if (party.getSheep().contains((Sheep) e.getEntity()))
				{
					e.setCancelled(true);
				}
			}
		}
	}

	// actually make sheep invincible
	@EventHandler
	public void onEntityDamageEvent(EntityDamageEvent e)
	{
		if (e.getEntity() instanceof Sheep)
		{
			for (DiscoParty party : parent.getParties())
			{
				if (party.getSheep().contains((Sheep) e.getEntity()))
				{
					{
						party.jumpSheep((Sheep) e.getEntity());
						e.setCancelled(true);
					}
				}
			}
		}
	}

	@EventHandler
	public void onPlayerQuitEvent(PlayerQuitEvent e)
	{
		String name = e.getPlayer().getName();
		parent.stopParty(name);
	}

	@EventHandler
	public void onPlayerJoinEvent(PlayerJoinEvent e)
	{
		if(DiscoParty.defaultPartyOnJoin)
		{
			Player p = e.getPlayer();
			boolean fireworks = true;
			int sheepNumber = DiscoParty.defaultSheep;
			int radius = DiscoParty.defaultRadius;
			int duration = DiscoParty.defaultDuration;
			int period = DiscoParty.defaultPeriod;
			parent.startParty(p, duration, sheepNumber, radius, period, fireworks);
		}
	}
}
