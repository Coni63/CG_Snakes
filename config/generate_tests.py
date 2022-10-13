import json
import random
import numpy as np

sizes = [
    [7, 7, "S", 2],
    [10, 10, "M", 3],
    [15, 10, "L", 4],
    [28, 15, "XL", 4]
]

for isTest in [True, False]:
    for width, height, size, max_snake in sizes:
        for num_snakes in range(1, max_snake+1):
            seed = random.randint(0, 2147483647)
            vals = [str(seed),str( width), str(height), str(num_snakes)]

            for snake in range(num_snakes):
                # le check de la validité est fait a la main :( ... flemme
                col = random.randint(2, width-3)
                row = random.randint(2, height-3)
                dir = random.choice("ULDR")
                    
                vals.append(f"{row} {col} {dir}")

            data = {
                "title": {
                    "2": f"{num_snakes} snakes - {size}",
                    "1": f"{num_snakes} snakes - {size}"
                },
                "testIn": "\n".join(vals),
                "isTest": isTest,
                "isValidator": not isTest
            }

            # if isTest:
            #     with open(f"{num_snakes}_snakes_{size}_T.json", "w") as f:
            #         json.dump(data, f, indent=4)
            # else:
            #     with open(f"{num_snakes}_snakes_{size}.json", "w") as f:
            #         json.dump(data, f, indent=4)

            # print(f"{num_snakes}_snakes_{size}.json")