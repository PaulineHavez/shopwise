## Project

The project Shopwise is a web and mobile app that centralizes inventory management, sales, appointments, and customer loyalty programs for local retailers within a given city.

## Git Workflow

This project follows a **GitFlow** workflow.

### Main branches

| Branch    | Description                               |
| --------- | ----------------------------------------- |
| `main`    | Stable version, ready to deploy           |
| `develop` | Integration branch, version under testing |

### Feature branches

Created from `develop` for each user story, fix or feature to do

### Rules

- **Never push directly** on `main` or `develop`
- Every feature → a dedicated branch → a Pull Request
- A PR can only be merged if **all tests pass**
- Merging `develop` → `main` = a new release

### Workflow

```
main
 ↑ PR (release)
develop
 ↑ PR (feature done)
feat/short-description
```
