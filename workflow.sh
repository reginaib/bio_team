# make sure you have proper classpath variable with paths to compiled project and argparse4j.jar

# show help msg
java BioTeam.CLI -h

# technician interface
java BioTeam.CLI technician -h

# create default repository with users
java BioTeam.CLI technician populate -f data/team.txt

# set optimal alignment and set it to all users
java BioTeam.CLI technician alignment -f data/hiv.fasta

# list users
java BioTeam.CLI technician list

# backup repo
java BioTeam.CLI technician backup -o backup

# accidentally remove user
java BioTeam.CLI technician del -n "Marc Janssens"
java BioTeam.CLI technician list

# restore repo
java BioTeam.CLI technician restore -f -i backup
java BioTeam.CLI technician list

# user interface
java BioTeam.CLI bioinformatician -h

# search sequence
java BioTeam.CLI bioinformatician -n "Marc Janssens" find -s ACCTGTCCGCTCGGGACCCAAATTAACGCAGAACCA
# replace sequence
java BioTeam.CLI bioinformatician -n "Marc Janssens" replace -s ACCTGTCCGCTCGGGACCCAAATTAACGCAGAACCA -r ACCTGTCCGCTCGGGACCCAAATTAACGCAGAATTT

# replace genome
java BioTeam.CLI bioinformatician -n "Marc Janssens" replace-genome -f data/gene.fasta -id 1990.U.CD.90.90CD121E12

# write SNIP to default file
java BioTeam.CLI bioinformatician -n "Marc Janssens" write-snip

# leader interface
java BioTeam.CLI leader -h

# write score
java BioTeam.CLI leader score

# write FASTA into default file
java BioTeam.CLI leader fasta

# pick new optimal alignment
java BioTeam.CLI leader promote -n "Marc Janssens"
