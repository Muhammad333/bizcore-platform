# Contributing to BizCore Platform

First off, thank you for considering contributing to BizCore Platform! It's people like you that make BizCore such a great tool.

## 📋 Table of Contents

- [Code of Conduct](#code-of-conduct)
- [How Can I Contribute?](#how-can-i-contribute)
- [Development Setup](#development-setup)
- [Coding Standards](#coding-standards)
- [Commit Guidelines](#commit-guidelines)
- [Pull Request Process](#pull-request-process)
- [Reporting Bugs](#reporting-bugs)
- [Suggesting Enhancements](#suggesting-enhancements)
- [Adding Translations](#adding-translations)

---

## Code of Conduct

This project and everyone participating in it is governed by our Code of Conduct. By participating, you are expected to uphold this code. Please report unacceptable behavior to [conduct@bizcore.com](mailto:conduct@bizcore.com).

### Our Pledge

- Be respectful and inclusive
- Accept constructive criticism gracefully
- Focus on what is best for the community
- Show empathy towards other community members

---

## How Can I Contribute?

### 🐛 Reporting Bugs

Before creating bug reports, please check the [issue tracker](https://github.com/yourusername/bizcore-platform/issues) as you might find that the bug has already been reported.

When creating a bug report, please include:

- **Clear title** - A concise description of the issue
- **Steps to reproduce** - Detailed steps to reproduce the behavior
- **Expected behavior** - What you expected to happen
- **Actual behavior** - What actually happened
- **Screenshots** - If applicable
- **Environment** - OS, Java version, PostgreSQL version, browser
- **Additional context** - Any other relevant information

**Example:**

```markdown
**Title:** User role selection not saving

**Steps to reproduce:**
1. Login as admin
2. Go to Users page
3. Click Edit on any user
4. Change roles selection
5. Click Save

**Expected:** Roles should be updated
**Actual:** Roles revert to previous selection

**Environment:** Windows 11, Java 17, Chrome 120
```

### ✨ Suggesting Enhancements

Enhancement suggestions are tracked as GitHub issues. When creating an enhancement suggestion, please include:

- **Clear title** - A concise description of the enhancement
- **Use case** - Why this enhancement would be useful
- **Proposed solution** - How you envision this working
- **Alternatives** - Any alternative solutions you've considered
- **Additional context** - Screenshots, mockups, or examples

### 🔧 Code Contributions

1. **Fork** the repository
2. **Create** a feature branch
3. **Make** your changes
4. **Test** your changes thoroughly
5. **Submit** a pull request

---

## Development Setup

### Prerequisites

- Java JDK 17+
- Maven 3.8+
- PostgreSQL 15+
- Node.js 18+ (for frontend development)
- Git

### Clone and Setup

```bash
# Fork the repo on GitHub, then clone your fork
git clone https://github.com/YOUR_USERNAME/bizcore-platform.git
cd bizcore-platform

# Add upstream remote
git remote add upstream https://github.com/yourusername/bizcore-platform.git

# Create database
psql -U postgres -c "CREATE DATABASE bizcore_db;"

# Run database setup
cd database
psql -U postgres -d bizcore_db -f core/tables.sql
psql -U postgres -d bizcore_db -f references/tables.sql
psql -U postgres -d bizcore_db -f init-database.sql
psql -U postgres -d bizcore_db -f translations.sql
cd ..

# Configure application
cd bizcore/src/main/resources
# Edit application.yml with your database credentials
cd ../../..

# Build project
mvn clean install

# Run application
mvn spring-boot:run
```

### Frontend Development

```bash
cd bizcore/webapp

# Install dependencies
npm install

# Start development server with hot reload
npm run dev

# Frontend will be at http://localhost:5173
# API proxied to http://localhost:8091
```

---

## Coding Standards

### Java Backend

#### Style Guide

- **Indentation:** 4 spaces (no tabs)
- **Line length:** Max 120 characters
- **Braces:** Opening brace on same line
- **Naming:**
  - Classes: PascalCase (`UserService`)
  - Methods/Variables: camelCase (`getUserById`)
  - Constants: UPPER_SNAKE_CASE (`MAX_LOGIN_ATTEMPTS`)
  - Packages: lowercase (`com.bizcore.service`)

#### Best Practices

```java
// ✅ Good
@Service
public class UserService {
    private final UserRepository userRepository;

    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public UserDTO getUserById(Long id) {
        return userRepository.findById(id)
            .map(this::convertToDTO)
            .orElseThrow(() -> new NotFoundException("User not found"));
    }
}

// ❌ Bad
@Service
public class UserService {
    @Autowired
    private UserRepository userRepository; // Use constructor injection

    public UserDTO getUserById(Long id) throws Exception { // Don't throw generic Exception
        User user = userRepository.findById(id).get(); // Don't use .get() without checking
        return new UserDTO(); // No conversion logic
    }
}
```

#### Documentation

```java
/**
 * Retrieves a user by their unique identifier.
 *
 * @param id the user ID to search for
 * @return the user DTO if found
 * @throws NotFoundException if user doesn't exist
 */
public UserDTO getUserById(Long id) {
    // Implementation
}
```

### React Frontend

#### Style Guide

- **Indentation:** 2 spaces
- **Line length:** Max 100 characters
- **Quotes:** Single quotes for strings
- **Semicolons:** Not required but consistent
- **Naming:**
  - Components: PascalCase (`UserList`)
  - Functions/Variables: camelCase (`getUserList`)
  - Constants: UPPER_SNAKE_CASE (`API_BASE_URL`)
  - CSS classes: kebab-case (`user-list-item`)

#### Best Practices

```jsx
// ✅ Good - Functional component with hooks
import { useState, useEffect } from 'react'
import api from '../utils/api'

export default function UserList() {
  const [users, setUsers] = useState([])
  const [loading, setLoading] = useState(true)

  useEffect(() => {
    loadUsers()
  }, [])

  const loadUsers = async () => {
    try {
      setLoading(true)
      const data = await api.get('/users')
      setUsers(data)
    } catch (error) {
      console.error('Error loading users:', error)
    } finally {
      setLoading(false)
    }
  }

  if (loading) return <div>Loading...</div>

  return (
    <div className="user-list">
      {users.map(user => (
        <div key={user.id}>{user.name}</div>
      ))}
    </div>
  )
}

// ❌ Bad
class UserList extends Component {
  componentDidMount() {
    fetch('/api/users').then(r => r.json()).then(users => {
      this.setState({users: users}) // Prefer functional components
    })
  }

  render() {
    return <div>{this.state.users.map(u => <div>{u.name}</div>)}</div> // Missing keys
  }
}
```

#### Component Documentation

```jsx
/**
 * UserList component displays a paginated list of users.
 *
 * @component
 * @param {Object} props - Component props
 * @param {number} props.companyId - Filter users by company
 * @param {Function} props.onUserSelect - Callback when user is selected
 * @returns {JSX.Element} The rendered component
 */
export default function UserList({ companyId, onUserSelect }) {
  // Implementation
}
```

---

## Commit Guidelines

We follow [Conventional Commits](https://www.conventionalcommits.org/) specification.

### Commit Message Format

```
<type>(<scope>): <subject>

<body>

<footer>
```

### Types

- `feat`: New feature
- `fix`: Bug fix
- `docs`: Documentation changes
- `style`: Code style changes (formatting, semicolons, etc.)
- `refactor`: Code refactoring
- `perf`: Performance improvements
- `test`: Adding or updating tests
- `chore`: Maintenance tasks

### Examples

```bash
feat(users): add multi-select for role assignment

Replaced HTML select element with custom MultiSelect component
for better UX when assigning roles to users.

Closes #123

---

fix(auth): prevent empty authorities authentication

Added validation to block users without roles/permissions
from authenticating.

---

docs(readme): add installation instructions

Added detailed setup guide for Windows and Linux users.

---

refactor(api): extract common error handling

Created GlobalExceptionHandler to centralize error responses.
```

### Rules

- Use present tense ("add feature" not "added feature")
- Use imperative mood ("move cursor" not "moves cursor")
- First line max 72 characters
- Reference issues and PRs when applicable
- Explain **why** not just **what**

---

## Pull Request Process

### Before Submitting

1. **Update from upstream**
   ```bash
   git fetch upstream
   git rebase upstream/main
   ```

2. **Run tests**
   ```bash
   mvn test
   cd webapp && npm test
   ```

3. **Check code style**
   ```bash
   mvn checkstyle:check
   cd webapp && npm run lint
   ```

4. **Update documentation** if needed

5. **Add tests** for new features

### Submitting PR

1. **Push to your fork**
   ```bash
   git push origin feature/your-feature
   ```

2. **Create PR** on GitHub

3. **Fill PR template**
   - Description of changes
   - Related issue numbers
   - Screenshots (if UI changes)
   - Testing checklist

### PR Template

```markdown
## Description
Brief description of what this PR does.

## Related Issues
Closes #123

## Type of Change
- [ ] Bug fix
- [ ] New feature
- [ ] Breaking change
- [ ] Documentation update

## Testing Done
- [ ] Unit tests added/updated
- [ ] Integration tests pass
- [ ] Manual testing completed
- [ ] Tested on multiple browsers (if UI)

## Screenshots (if applicable)
[Add screenshots here]

## Checklist
- [ ] Code follows project style guidelines
- [ ] Self-review completed
- [ ] Comments added for complex logic
- [ ] Documentation updated
- [ ] No new warnings introduced
```

### Review Process

- At least 1 approval required
- CI/CD checks must pass
- No merge conflicts
- Documentation updated

---

## Reporting Bugs

Use the [bug report template](.github/ISSUE_TEMPLATE/bug_report.md).

### Security Vulnerabilities

**Do not** create public issues for security vulnerabilities. Instead, email [security@bizcore.com](mailto:security@bizcore.com) with:

- Description of the vulnerability
- Steps to reproduce
- Potential impact
- Suggested fix (if any)

---

## Suggesting Enhancements

Use the [feature request template](.github/ISSUE_TEMPLATE/feature_request.md).

### What Makes a Good Feature Request

- **Clear use case** - Why is this needed?
- **Specific solution** - What exactly should happen?
- **Consider alternatives** - Are there other ways to solve this?
- **Backwards compatibility** - Will this break existing functionality?

---

## Adding Translations

To add a new language:

1. **Add translation keys** to `database/translations.sql`:

```sql
-- Spanish translations
INSERT INTO translations (key, language, value, module) VALUES
('common.save', 'es', 'Guardar', 'common'),
('common.cancel', 'es', 'Cancelar', 'common'),
-- ... more translations

ON CONFLICT (key, language) DO UPDATE SET
    value = EXCLUDED.value,
    updated_at = NOW();
```

2. **Run the SQL file**:
```bash
psql -U postgres -d bizcore_db -f database/translations.sql
```

3. **Update frontend** if needed for language selector

4. **Test** all UI elements in the new language

### Translation Guidelines

- Keep translations concise
- Maintain consistent terminology
- Consider cultural context
- Test UI layout (some languages are longer)
- Add comments for ambiguous terms

---

## Development Workflow

### Feature Development

```bash
# 1. Create feature branch
git checkout -b feature/add-notification-system

# 2. Make changes
# ... code ...

# 3. Commit changes
git add .
git commit -m "feat(notifications): add real-time notification system"

# 4. Push to your fork
git push origin feature/add-notification-system

# 5. Create PR on GitHub
```

### Bug Fix

```bash
# 1. Create fix branch
git checkout -b fix/user-role-save-issue

# 2. Fix the bug
# ... code ...

# 3. Add test
# ... test code ...

# 4. Commit
git commit -m "fix(users): resolve role save issue

Added validation before save and fixed state update logic.

Closes #456"

# 5. Push and create PR
git push origin fix/user-role-save-issue
```

---

## Testing Requirements

### Backend Tests

```java
@Test
public void testGetUserById_Success() {
    // Arrange
    User user = createTestUser();
    when(userRepository.findById(1L)).thenReturn(Optional.of(user));

    // Act
    UserDTO result = userService.getUserById(1L);

    // Assert
    assertNotNull(result);
    assertEquals("admin", result.getUsername());
}

@Test
public void testGetUserById_NotFound() {
    // Arrange
    when(userRepository.findById(999L)).thenReturn(Optional.empty());

    // Act & Assert
    assertThrows(NotFoundException.class, () -> {
        userService.getUserById(999L);
    });
}
```

### Frontend Tests

```jsx
import { render, screen, waitFor } from '@testing-library/react'
import userEvent from '@testing-library/user-event'
import UserList from './UserList'

test('renders user list', async () => {
  render(<UserList />)

  await waitFor(() => {
    expect(screen.getByText('admin')).toBeInTheDocument()
  })
})

test('selects user on click', async () => {
  const onSelect = jest.fn()
  render(<UserList onUserSelect={onSelect} />)

  const user = screen.getByText('admin')
  await userEvent.click(user)

  expect(onSelect).toHaveBeenCalledWith(expect.objectContaining({
    username: 'admin'
  }))
})
```

---

## Questions?

- **Documentation**: Check [docs/](docs/) folder
- **Discussions**: Use [GitHub Discussions](https://github.com/yourusername/bizcore-platform/discussions)
- **Chat**: Join our [Discord server](https://discord.gg/bizcore)
- **Email**: dev@bizcore.com

---

## Recognition

Contributors will be recognized in:
- README.md Contributors section
- Release notes
- Project website (coming soon)

---

Thank you for contributing to BizCore Platform! 🎉

Your efforts help make this project better for everyone.
