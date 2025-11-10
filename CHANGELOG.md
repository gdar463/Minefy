# Changelog

All notable changes to this project will be documented in this file. See [conventional commits](https://www.conventionalcommits.org/) for commit guidelines.

---
## [0.2.0](https://github.com/gdar463/Minefy/compare/v0.1.0..v0.2.0) - 2025-11-10

Player Buttons Update

### Bug Fixes

- **(sp-api)** skip requests are post not put ([bd8c561](https://github.com/gdar463/Minefy/commit/bd8c561bfe4f0d0f7064d09b947dd9ae59493145)) - @gdar463
- made PlaybackHUD start with client not at world change ([72989b7](https://github.com/gdar463/Minefy/commit/72989b793c1a490e4e63e0d7d653039220c1975d)) - @gdar463
- made user init in initializer not with process token ([e9b50c5](https://github.com/gdar463/Minefy/commit/e9b50c51f4b808ae30aed909c35ade8cb61c7443)) - @gdar463
- set disallows member to 0 when copying in SpotifyPlayer ([b06af58](https://github.com/gdar463/Minefy/commit/b06af58b3e3785347496fff5432f3b38ad435800)) - @gdar463
- fixed SpotifyPlayerDisallows flags being out of real order ([f608c94](https://github.com/gdar463/Minefy/commit/f608c947f037985873ff3ab3dbfe24257a165146)) - @gdar463
- fixed getPlayer spamming the API and getting rate-limited in 3 seconds ([bc61b77](https://github.com/gdar463/Minefy/commit/bc61b773437c664768bc7f929b5075275cd1c4c7)) - @gdar463
- fixed mouseCoords being a valid number when window is null in PlaybackHUD ([472ea84](https://github.com/gdar463/Minefy/commit/472ea84245dfaa1ec9000b4b04081402e96f7b73)) - @gdar463

### Chores

- **(lang)** reorganized lang file ([4580d13](https://github.com/gdar463/Minefy/commit/4580d1358f9009fff2fd6dd4093f7f3635b596de)) - @gdar463
- backported YACL version to 3.7.1 ([e9aace5](https://github.com/gdar463/Minefy/commit/e9aace530b5ef0ab606cdb2187d5fc06f917be4e)) - @gdar463
- added warning when rate limited by spotify in WrapperHttpClient ([c2c4eab](https://github.com/gdar463/Minefy/commit/c2c4eab7a0fb28d88ff1bb394ed68b4fcde0a0fc)) - @gdar463
- made almost all HudConfigCategory groups collapsed by default ([683c51c](https://github.com/gdar463/Minefy/commit/683c51cc54724a17a511f0d663a8f21892b95d45)) - @gdar463

### Features

- **(debug)** added testing items in SpotifyConfigCategory ([64f99d7](https://github.com/gdar463/Minefy/commit/64f99d7751464a68329b59239cc69ddb3fed620e)) - @gdar463
- **(sp-api)** added player actions i.e. resume/pause ([16df306](https://github.com/gdar463/Minefy/commit/16df30630e07ea9584c0cd42c61c42dc0b20b086)) - @gdar463
- **(sp-api)** added seekToPosition call ([9335440](https://github.com/gdar463/Minefy/commit/9335440bcc05fa15c281577a01c5bebf15c6929b)) - @gdar463
- added SpotifyUser to ensure premium user for actions ([578765e](https://github.com/gdar463/Minefy/commit/578765e8780b6988c7d62691491d9ed092462b9d)) - @gdar463
- added check for disallowed actions in SpotifyPlayer ([2146915](https://github.com/gdar463/Minefy/commit/2146915a22e112aa00d43a41cc58c5f1cf5376ae)) - @gdar463
- added new border color when HUD is hovered by cursor ([0f679f4](https://github.com/gdar463/Minefy/commit/0f679f43878ce5e5315922dc6803453efee6ac3f)) - @gdar463
- added resizing of HUD when hovered ([32c2704](https://github.com/gdar463/Minefy/commit/32c27044b838e943b0916cc64a5a126c6def9bae)) - @gdar463
- added buttons when HUD is hovered ([ae56c2e](https://github.com/gdar463/Minefy/commit/ae56c2e4fa6e8b01a2ea7479105c60afa3a3b5a3)) - @gdar463
- added click action to player buttons ([811ed69](https://github.com/gdar463/Minefy/commit/811ed694206eb6ec038f9c3005b5d4074f7b6bff)) - @gdar463
- added updateInterval to SpotifyConfig ([fe5e86a](https://github.com/gdar463/Minefy/commit/fe5e86a29b1fead05d4573b135c334081ec6b6b1)) - @gdar463
- added buttons to config and semi-abstracted their rendering ([349a9bd](https://github.com/gdar463/Minefy/commit/349a9bd637c59a2d6f7a5b8346226e53633b72a3)) - @gdar463
- added coords preset system ([d2fe45c](https://github.com/gdar463/Minefy/commit/d2fe45c01855049edcf6af4a442997a517280ba1)) - @gdar463

### Style

- **(readme)** fixed case typo ([80f111f](https://github.com/gdar463/Minefy/commit/80f111fca1487ae83263dfea5b487a14bed27525)) - @gdar463
- changed text for Spotify not yet linked ([30ce039](https://github.com/gdar463/Minefy/commit/30ce03901bb4ab6872c371e22821bf4ab99bf6c0)) - @gdar463

---
## [0.1.0] - 2025-10-13

Initial Release

