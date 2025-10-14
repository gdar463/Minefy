# Contributing

To contribute to this repo, you implictly agree to the [Code of Conduct](CODE_OF_CONDUCT.md) and to sign off every commit
with name and email to affirm that your commit complies with the [Developer Certificate of Origin](DOC.md).

This repo follows the [Conventional Commits](https://www.conventionalcommits.org/) standard,
to allow better descriptions of changes and automatic CHANGELOG generation.

To ensure this, a Gradle plugin (it.nicolasfarabegoli.conventional-commits) enforces all commit messages
to follow the correct usage of the standard.

## Commit Types

| Type Name    | Description        | Meaning                                                                           |
|--------------|--------------------|-----------------------------------------------------------------------------------|
| \<type\>!:   | BREAKING CHANGE    | BIG change happened, WILL break other mods (probably not used in this project)    |
| feat:        | new feature        | Big change happened, enough to guarantee a new version number                     |
| fix:         | bug fixes          | Bug fix happened, enough to guarantee a small version increase                    |
| docs:        | documentation      | Updates to documentation, no new mod releases                                     |
| build:       | gradle changes     | Changes to Gradle building process, might break local repos                       |
| ci:          | ci changes         | Changes to Github Actions workflows, no new mod releases                          |
| refactor:    | refactorings       | Refactored code, but no new features, release only if another incoming            |
| style:       | style change       | Changed code styling, but no new features, release only if another incoming       |
| chore:       | minor changes      | Small change happened, wait for a few before new release                          |
| chore(skip): | VERY minor changes | VERY small change happened, skipped by CHANGELOG (try to not use it for mod code) |