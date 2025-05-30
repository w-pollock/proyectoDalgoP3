"""Autores: 1. Nicolas Hernandez - 202322148
            2. William Pollock - 202221321
"""
def calc_overlap(a, b):
    max_ov = 0
    for i in range(1, min(len(a), len(b)) + 1):
        if a[-i:] == b[:i]:
            max_ov = i
    return max_ov

def build_superstring(current, remaining, max_len):
    if len(current) > max_len:
        return None
    if not remaining:
        return current if len(current) <= max_len else None

    for i, s in enumerate(remaining):
        overlap = calc_overlap(current, s)
        merged = current + s[overlap:]
        result = build_superstring(merged, remaining[:i] + remaining[i+1:], max_len)
        if result:
            return result

    return None

def exists_superstring_of_length(m, cadenas):
    for i, s in enumerate(cadenas):
        result = build_superstring(s, cadenas[:i] + cadenas[i+1:], m)
        if result:
            return result
    return None

def find_min_superstring(cadenas):
    n = len(cadenas)
    k = len(cadenas[0])
    lo = k
    hi = n * k
    answer = None

    while lo < hi:
        mid = (lo + hi) // 2
        result = exists_superstring_of_length(mid, cadenas)
        if result:
            hi = mid
            answer = result
        else:
            lo = mid + 1

    return answer if answer else exists_superstring_of_length(lo, cadenas)

# --------------------------------------
# LECTURA DESDE ENTRADA ESTÁNDAR
# --------------------------------------
def main():
    import sys
    input = sys.stdin.read
    data = input().splitlines()

    idx = 0
    t = int(data[idx])
    idx += 1

    for _ in range(t):
        n, k = map(int, data[idx].split())
        idx += 1
        cadenas = []
        for _ in range(n):
            cadenas.append(data[idx])
            idx += 1
        resultado = find_min_superstring(cadenas)
        print(resultado)

if __name__ == "__main__":
    main()