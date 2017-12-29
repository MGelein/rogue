## Design Doc for Roguelike 2D
This document contains some thoughts about this small game prototype

### Classes
3 archetypes: __magic, stealth & melee__

Each archetype has 4 classes:

__Magic:__
- Mage			*(Magic)*
> Basic pure mage. No armor, lots of spells
- Conjurer		*(Magic)*
> Basic pure summoner, protected by minions
- Battlemage	*(Magic / Melee)*
> Mage in heavy armor with a blunt weapon
- Illusionist	*(Magic / Stealth)*
> Mage that prefers not to be seen, uses Illusions.

__Melee:__
- Warrior		*(Melee)*
> Basic pure warrior. Heavy armor, lots of health and defence
- Barbarian		*(Melee)*
> Basic pure warrior. Light armor, damage over defence
- Paladin		*(Melee / Magic)*
> Warrior with some spells. Heavy armor, healing and anti undead
- Scout			*(Melee / Stealth)*
> Warrior with some ranged. Light armor, uses a bow when necessary

__Stealth:__
- Rogue			*(Stealth)*
> Pure Stealth. Double dagger, light armor. Sneak attack x15602 damage.
- Thief			*(Stealth)*
> Pure Stealth. No armor. Never be seen. Trap detect, creation & barter skills
- Ranger		*(Stealth / Melee)*
> Stealth with some melee. Good use of a bow and shortsword. Think Legolas.
- Alchemist		*(Stealth / Magic)*
> Stealth with some magic. Uses potions, poisons and runes