Simple Mode
-----------

The "simple" mode restricts "freeplay" by introducing a more strict
state model, thus relieving both client and server from having to
track IDs.

As such "simple" mode is adequate for implementing tournaments.

Simple commands
---------------

`state [board]` (server)

: The server MUST ensure that only one request is handled by the
  client at any time. Before sending out the next request, either the
  client MUST have sent `yield` or have confirmed a `stop` command.

`stop` (server)

: The client MUST respond with `ok` before a new state is generated.

The commands `move` and `yield` are analogous to "Freeplay" mode, with
the exception that command references are not necessary.