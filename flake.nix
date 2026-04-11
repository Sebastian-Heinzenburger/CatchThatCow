{
  description = "Catch That Cow - A text-based animal catching game";

  inputs = {
    nixpkgs.url = "github:NixOS/nixpkgs/nixos-unstable";
    flake-utils.url = "github:numtide/flake-utils";
  };

  outputs = { self, nixpkgs, flake-utils }:
    flake-utils.lib.eachDefaultSystem (system:
      let
        pkgs = nixpkgs.legacyPackages.${system};
      in
      {
        devShells.default = pkgs.mkShell {
          buildInputs = with pkgs; [
            jdk21
            maven
          ];

          shellHook = ''
            echo "🐄 Catch That Cow Development Environment"
            echo "Java version: $(java -version 2>&1 | head -n 1)"
            echo "Maven version: $(mvn -version | head -n 1)"
            echo ""
            echo "Available commands:"
            echo "  mvn clean compile   - Compile the project"
            echo "  mvn clean package   - Build JAR"
            echo "  mvn exec:java       - Run the game"
            echo ""
          '';

          JAVA_HOME = "${pkgs.jdk21}";
        };
      }
    );
}
