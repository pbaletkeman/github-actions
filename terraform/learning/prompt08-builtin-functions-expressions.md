# Built-in Functions and Expressions

> **HashiCorp Certified Terraform Associate (004)**
> Objective 4 — Use Terraform Outside of Core Workflow

---

## Overview

Terraform provides a rich set of built-in functions for string manipulation, numeric operations, collection handling, encoding, filesystem access, IP addressing, and conditional logic. All functions can be tested interactively with `terraform console`.

---

## Testing with `terraform console`

```bash
terraform console
> upper("hello")
"HELLO"
> length(["a", "b", "c"])
3
> cidrsubnet("10.0.0.0/16", 8, 1)
"10.0.1.0/24"
```

---

## String Functions

| Function | Description | Example |
|----------|-------------|---------|
| `upper(s)` | Uppercase | `upper("hello")` → `"HELLO"` |
| `lower(s)` | Lowercase | `lower("HELLO")` → `"hello"` |
| `trimspace(s)` | Remove leading/trailing whitespace | `trimspace("  hi  ")` → `"hi"` |
| `trim(s, chars)` | Remove chars from start/end | `trim("--hello--", "-")` → `"hello"` |
| `split(sep, s)` | Split string into list | `split(",", "a,b,c")` → `["a","b","c"]` |
| `join(sep, list)` | Join list into string | `join(",", ["a","b"])` → `"a,b"` |
| `replace(s, sub, rep)` | Replace substring | `replace("hello", "l", "r")` → `"herro"` |
| `format(fmt, ...)` | Printf-style formatting | `format("Hello, %s!", "world")` → `"Hello, world!"` |
| `formatlist(fmt, list)` | Format each element | `formatlist("Hello, %s!", ["A","B"])` |
| `startswith(s, prefix)` | True if string starts with prefix | `startswith("hello", "he")` → `true` |
| `endswith(s, suffix)` | True if string ends with suffix | `endswith("hello", "lo")` → `true` |
| `contains(list, value)` | True if list contains value | `contains(["a","b"], "a")` → `true` |
| `indent(spaces, s)` | Indent each line | Used in templates |
| `templatefile(path, vars)` | Render template file with variables | `templatefile("user_data.sh.tpl", {name="web"})` |

---

## Numeric Functions

| Function | Description |
|----------|-------------|
| `abs(n)` | Absolute value |
| `ceil(n)` | Round up to integer |
| `floor(n)` | Round down to integer |
| `max(a, b, ...)` | Maximum value |
| `min(a, b, ...)` | Minimum value |
| `pow(base, exp)` | Power |
| `signum(n)` | -1, 0, or 1 |
| `parseint(s, base)` | Parse string as integer in given base |

---

## Collection Functions (HIGH EXAM PRIORITY)

| Function | Description | Example |
|----------|-------------|---------|
| `length(collection)` | Number of elements | `length(["a","b"])` → `2` |
| `keys(map)` | List of map keys | `keys({a=1,b=2})` → `["a","b"]` |
| `values(map)` | List of map values | `values({a=1,b=2})` → `[1,2]` |
| `lookup(map, key, default)` | Get map value or default | `lookup({a=1}, "b", 0)` → `0` |
| `merge(maps...)` | Merge maps (later keys win) | `merge({a=1},{b=2})` → `{a=1,b=2}` |
| `concat(lists...)` | Concatenate lists | `concat(["a"],["b"])` → `["a","b"]` |
| `flatten(list)` | Flatten nested lists | `flatten([[1,2],[3]])` → `[1,2,3]` |
| `distinct(list)` | Remove duplicates | `distinct(["a","a","b"])` → `["a","b"]` |
| `compact(list)` | Remove null/empty strings | `compact(["a","","b"])` → `["a","b"]` |
| `slice(list, start, end)` | Extract sublist | `slice(["a","b","c"], 0, 2)` → `["a","b"]` |
| `reverse(list)` | Reverse list | `reverse([1,2,3])` → `[3,2,1]` |
| `sort(list)` | Sort list alphabetically | `sort(["c","a","b"])` → `["a","b","c"]` |
| `element(list, index)` | Get element by index (wraps) | `element(["a","b","c"], 4)` → `"b"` |
| `index(list, value)` | Find index of value | `index(["a","b"], "b")` → `1` |
| `zipmap(keys, values)` | Create map from two lists | `zipmap(["a","b"],[1,2])` → `{a=1,b=2}` |
| `toset(list)` | Convert list to set (removes dupes) | `toset(["a","a","b"])` → `{"a","b"}` |
| `tolist(set)` | Convert set to list | |
| `tomap(object)` | Convert object to map | |
| `setproduct(sets...)` | Cartesian product | `setproduct(["a"],["x","y"])` |

---

## Encoding Functions

| Function | Description |
|----------|-------------|
| `jsonencode(value)` | Encode value as JSON string |
| `jsondecode(str)` | Parse JSON string to value |
| `base64encode(str)` | Base64 encode |
| `base64decode(str)` | Base64 decode |
| `yamlencode(value)` | Encode as YAML |
| `yamldecode(str)` | Parse YAML string |

---

## Filesystem Functions

```hcl
# Read a file's content as a string
user_data = file("${path.module}/user_data.sh")

# Render a template file with variables
user_data = templatefile("${path.module}/user_data.sh.tpl", {
  environment = var.environment
  app_name    = local.app_name
})
```

| Path Variable | Description |
|--------------|-------------|
| `path.module` | Path of the current module |
| `path.root` | Path of the root module |
| `path.cwd` | Current working directory |

---

## Date and Time Functions

| Function | Description | Example |
|----------|-------------|---------|
| `timestamp()` | Current UTC timestamp as RFC 3339 | `"2024-01-15T10:30:00Z"` |
| `timeadd(ts, duration)` | Add duration to timestamp | `timeadd("2024-01-01T00:00:00Z", "24h")` |
| `formatdate(format, ts)` | Format timestamp | `formatdate("YYYY-MM-DD", timestamp())` |

---

## IP Network Functions (HIGH EXAM PRIORITY)

These are commonly tested for VPC/subnet design.

| Function | Description | Example |
|----------|-------------|---------|
| `cidrsubnet(prefix, newbits, netnum)` | Calculate subnet CIDR | `cidrsubnet("10.0.0.0/16", 8, 1)` → `"10.0.1.0/24"` |
| `cidrhost(prefix, hostnum)` | Calculate host address | `cidrhost("10.0.1.0/24", 5)` → `"10.0.1.5"` |
| `cidrnetmask(prefix)` | Get subnet mask | `cidrnetmask("10.0.0.0/24")` → `"255.255.255.0"` |

### `cidrsubnet` Explained

```
cidrsubnet("10.0.0.0/16", 8, 1)
           │              │  │
           │              │  └── subnet number (0, 1, 2, ...)
           │              └───── additional bits to borrow (16+8 = /24 subnets)
           └──────────────────── base CIDR
Result: "10.0.1.0/24"
```

---

## Conditional Expression

```hcl
condition ? true_value : false_value

# Examples
instance_type = var.environment == "production" ? "t3.large" : "t3.micro"

count = var.create_instance ? 1 : 0
```

---

## `can` and `try` Functions

```hcl
# can(expr) — returns true if expression evaluates without error
can(var.settings["key"])   # true if key exists, false if error

# try(expr1, expr2, ...) — returns first non-erroring result
local.value = try(var.settings["optional_key"], "default_value")
```

---

## Exam Checklist

- [ ] Know `terraform console` is for interactive function testing
- [ ] Know the high-priority collection functions: length, lookup, merge, concat, flatten, distinct, compact, zipmap, toset
- [ ] Know `lookup(map, key, default)` — always provide default to avoid errors
- [ ] Know `flatten()` removes nesting from lists of lists
- [ ] Know `compact()` removes empty strings and nulls
- [ ] Know encoding functions: jsonencode/jsondecode, base64encode/base64decode
- [ ] Know `file(path)` reads file content; `templatefile(path, vars)` renders a template
- [ ] Know `cidrsubnet(prefix, newbits, netnum)` for subnet calculation
- [ ] Know conditional expression syntax: `condition ? true : false`
- [ ] Know `try(expr, fallback)` returns fallback if expr errors

---

[⬅️ prompt07-complex-types-collections.md](prompt07-complex-types-collections.md) | **8 / 17** | [Next ➡️ prompt09-dependencies-lifecycle.md](prompt09-dependencies-lifecycle.md)
