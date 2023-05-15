help() -> (
	print('/rename §b-§f A half-baked, alternate way of renaming items');
	print('/rename preview§7/§rpr §b-§f Preview name into chat');
	print(format('w Supports ','uy formatting codes','!/rename formatting','^y /rename formatting','w  through the use of ampersand §7(&)'));
	print('Example: /rename §7&1§rC§7&2§ro§7&3§rl§7&4§ro§7&5§rr§7&6§rm§7&7§ra§7&8§rt§7&9§ri§7&a§rc§7&b§r!');
	print('                   §b->§f §1C§2o§3l§4o§5r§6m§7a§8t§9i§ac§b!')
);

formatting() -> (
	print('§lFormatting codes');
	print(format('w You can check the ','uy wiki page','@https://minecraft.fandom.com/wiki/Formatting_codes','^y https://minecraft.fandom.com/wiki/Formatting_codes','w  for more information.'));
	print('§00 §11 §22 §33 §44 §55 §66 §77 §88 §99 §aa §bb §cc §dd §ee §ff§r');
	print('r §b-§f Reset§r         l §b-§f §lBold§r               o §b-§f §oItalic§r');
	print('n §b-§f §nUnderline§r    m §b-§f §mStrikethrough§r    k §b-§f §kObfuscated')
);

rename(name) -> (
	p = player();
	item = p~'holds';
	nbt = item:2;
	if (nbt == null, nbt = nbt('{}'));
	put(nbt, 'display.Name', '\'{"text":"'+formatify(name)+'""}\'');

	if (!item, return(throw_error('§cYou aren\'t holding an item!')));
	if (item:2 == nbt, return(throw_error('§cItem name is identical!')));
	if (p~'xp_level' == 0 && p~'gamemode' == 'survival', return(throw_error('§cYou need at least 1 xp level!')));

	if (p~'gamemode' == 'survival', modify(p,'xp_level',p~'xp_level'-1));
	inventory_set(p,p~'selected_slot', item:1, item:3, nbt);

	sound('minecraft:block.conduit.activate', p~'pos');
	particle('minecraft:ambient_entity_effect',p~'pos', 100, 0.1, 0.1);
	particle('minecraft:firework', p~'pos', 100, 0, 0.2)
);

preview(name) -> print(formatify(name));

formatify(s) -> (
	while (s~'&\\S', 65536, s = replace(s, s~'&\\S', replace(s~'&\\S', '&', '§')));
	return (s);
);

throw_error(text) -> (
	p = player();
	display_title(p, 'actionbar', text);
	sound('minecraft:block.shroomlight.break', p~'pos')
);

__config() -> {
	'commands' -> {
		'' -> 'help',
		'help' -> 'help',
		'<name>' -> 'rename',
		'preview <name>' -> 'preview',
		'pr <name>' -> 'preview',
		'formatting' -> 'formatting'
	},
	'arguments' -> {
		'name' -> { 'type' -> 'text', 'suggest' -> [] }
	}
}
