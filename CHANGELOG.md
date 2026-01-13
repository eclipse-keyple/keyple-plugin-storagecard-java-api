# Changelog

All notable changes to this project will be documented in this file.

The format is based on [Keep a Changelog](https://keepachangelog.com/en/1.0.0/),
and this project adheres to [Semantic Versioning](https://semver.org/spec/v2.0.0.html).

## [Unreleased]
### Added
- Documentation of recommended status words for ApduInterpreterSpi implementations
- Exception handling guidelines for CommandProcessorApi implementations
- Added `loadKey` method to `CommandProcessorApi` interface for loading card-specific authentication keys into reader
  memory (volatile or non-volatile).
- Added `generalAuthenticate` method to `CommandProcessorApi` interface for performing authentication to contactless
  cards using previously loaded keys.
### Changed
- Enhanced JavaDoc with comprehensive error handling information
- Added status word reference table following ISO 7816-4 and PC/SC standards

## [1.0.0] - 2025-07-08

This is the initial release.

[unreleased]: https://github.com/eclipse-keyple/keyple-plugin-storagecard-java-api/compare/1.0.0...HEAD

[1.0.0]: https://github.com/eclipse-keyple/keyple-plugin-storagecard-java-api/releases/tag/1.0.0
