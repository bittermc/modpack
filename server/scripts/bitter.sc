__config() -> {
	'bot' -> 'bittermc',
	'scope' -> 'global'
};

global_channel = dc_channel_from_id('829261859991060520');

__on_start() -> task(_() -> (
	print(';P');
	//dc_send_message(global_channel, 'bridge has been established!');
));

__on_close() -> task(_() -> (
	dc_send_message(global_channel, 'bridge is now closed.');
));

__on_player_message(p, content) -> (
	task(_(outer(p), outer(content)) -> (
		dc_send_message(global_channel, str('**<%s>** %s', p, content));
	))
);

__on_discord_message(msg) -> (
	if (msg~'channel' != global_channel || msg~'user' == dc_get_bot_user(), return());
	if (msg~'content'~'^b!', return(task(_(outer(msg)) -> (
		text = replace(msg~'content', '^b!', '');
		if (text~'^players',
			string = 'Players: ';
			for (player('all'), string += '\n' + _);
			return (dc_send_message(msg~'channel', string));
		);

		return(dc_send_message(msg~'channel', str('command **%s** does not exist', text~'^([\\w\-]+)')))
	))));

	user = msg~'user';

	print(player('all'),
		'§9['+format(str('t %s', user~'name'), str('^ ping %s in Discord', user~'name'), str('?<@%s>\ ', user~'id'))+'§9]§r '+msg~'content'
	);
);

__on_system_message(text, type) -> (
	if (!type || type~'chat.type.(text|admin)', return());
	task(_(outer(text), outer(type)) -> (
		dc_send_message(dc_channel_from_id, text);
	))
)