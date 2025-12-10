import pathlib
root = pathlib.Path('dragonball-backend/dragonball-backend/src/main/java')
count = 0
files = []
for path in root.rglob('*.java'):
    data = path.read_bytes()
    if data.startswith(b'\xef\xbb\xbf'):
        path.write_bytes(data[3:])
        count += 1
        files.append(path)
print('removed', count)
for p in files:
    print(p)
