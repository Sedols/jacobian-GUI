jacobian
========

A minimal computer contract bridge communication protocol and implementation.

The jacobian protocol
---------------------

The protocol supports four players and a host program.

In EBNF, every message here ends with a newline. The number
of brackets signifies the number of such messages being sent
and the direction specifies whether the recipient is a player
or the host.

Check Example.txt for details

    >>>> "jacobian"
    <<<< "id ", player_name
    >>>> "new" | "newbidding" | "newplay"

Assuming `newplay` mode (the only one currently implemented)...

    >>>> "seat ", "North" | "East" | "South" | "West"
    >>>> "vul ", "All" | "None" | "EW" | "NS"
    >>>> "hand ", hand
    >>>> "contract ", contract
    >    "go"
    <    "play ", card 
    >>>> "dummy ", hand
    >>>  "show ", card
    >    "go"
    <    "play ", card
    >>>  "show ", card
    >    "go"
    <    "play ", card
    >>>  "show ", card
         ...

    player_name = ascii_character, { ascii_character }
    contract = level, strain, double_status, seat
    double_status = "" | "X" | "XX"
    hand = holding, ".", holding, ".", holding, ".", holding
    holding = { rank }
    card = suit, rank
    rank = "A" | "K" | "Q" | "J" | "T" | "9" | "8" | "7" | "6" | "5" | "4" | "3" | "2"
    seat = "N" | "E" | "S" | "W"
    strain = suit | "N"
    level = "1" | "2" | "3" | "4" | "5" | "6" | "7"
    suit = "S" | "H" | "D" | "C"

Currently no support for:

* bidding
* different scoring/goals
* forming agreements
* disclosure

The jacobian host
-----------------

The host is an implementation of the jacobian protocol.

References
----------

* [Universal Chess Interface](http://www.shredderchess.com/chess-info/features/uci-universal-chess-interface.html)
* [Chess Engine Communication Protocol](http://www.open-aurec.com/wbforum/WinBoard/engine-intf.html)
* [Portable Bridge Notation](http://www.tistis.nl/pbn/)
* [Networking of Computer Bridge Programs](http://www.bluechipbridge.co.uk/protocol.htm)
