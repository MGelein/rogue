# Brief explanation of Magic
Magic effects need some variables declared. The following list are the variables:
1. Effect
1. Target
1. Magnitude
1. Duration
1. Data

## Effect
The effect of the magic spell / effect is a list of predefined spell effects that have been defined in the code
of the game. You can't make other effects in the data files. Possible effect types include:
- HEAL *(Heals for specified magnitude)*
- FIRE *(Fire damage for specified magnitude)*
- ICE *(Ice damage for specified magnitude)*
- EARTH *(Earth damage for specified magnitude)*
- LIGHTNING *(Lightniging.... )*
- WATER *(Water...)*
- LIGHT *(Light damage (good vs undead))*
- POSSES *(Posseses the target with a specified magnitude)*
- IDENTIFY *(Identifies any item in the inventory fully)*
- MM *(Shoots magic missiles)*
- CONJURE *(Creates a friendly version of a creature (DATA) for the summoners benefit)*
- SILENCE *(Casts an aura of silence around the target)*
- RESIST *(Gives the target a resistance (DATA))*
- FACTION *(Sets the faction of the target temporarily to DATA)*

## Target
An effect either selects a target for you or prompts you to select one. The following targets can be used:
- SELF	*(The user of the effect, i.e, heal self, shield self etc.)*
- NEAREST *(The target is selected by the computer to be the nearest character, not yourself.)*
- AOE *(Area of effect, this also has a range, how to define it?)*
- TARGET *(Prompts the user to select a target within range. Basically any tile within range or inventory)*

## Magnitude
Most spell types will need to define a magnitude. This is a numerical value that represents points healed, or
points damage taken, or the strength of the possesion.

## Duration
Argument that defines the duration over which the affect is applied. This duration is defined in seconds

## Data
This is an optional argument that defines variables for some spells, like resistance (to something), summoning (of something) 
and weakness (to something).
