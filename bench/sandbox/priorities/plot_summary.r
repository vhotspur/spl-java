#
# Copyright 2013 Charles University in Prague
# Copyright 2013 Vojtech Horky
#
# Licensed under the Apache License, Version 2.0 (the "License");
# you may not use this file except in compliance with the License.
# You may obtain a copy of the License at
#
#     http://www.apache.org/licenses/LICENSE-2.0
#
# Unless required by applicable law or agreed to in writing, software
# distributed under the License is distributed on an "AS IS" BASIS,
# WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
# See the License for the specific language governing permissions and
# limitations under the License.
#

#
# Usage: Rscript plot_summary.r --output=output.pdf *.csv
#

rprintf <- function(fmt, ...) {
	writeLines(sprintf(fmt=fmt, ...), sep="")
}

data <- list()

argv <- commandArgs(trailingOnly=TRUE)
i <- 1
output.file <- FALSE

for (arg in argv) {
	x <- strsplit(arg, split="=")[[1]]
	if (length(x) < 2) {
		x <- c(x, x)
	}
	
	if (x[1] == "--output") {
		output.file <- x[2]
		next
	}
	
	data[[i]] <- list(
		"filename" = x[1],
		"title" = x[2],
		"color" = rainbow(length(argv))[i]
	)
	i <- i + 1
}

for (i in 1:length(data)) {
	all <- read.csv(data[[i]][["filename"]], head=FALSE)
	all <- all[order(all[["V1"]]),];
	data[[i]][["latency"]] <- all[["V2"]]
	data[[i]][["timestamp"]] <- all[["V1"]] - min(all[["V1"]])
}

# Retrive legend titles and colors
data.colors <- sapply(data, function(d) { d[["color"]] })
data.titles <- sapply(data, function(d) { d[["title"]] })

xlim <- c(0,2)
ylim <- c(0,2)

for (d in data) {
	xlim[2] <- max(xlim[2], length(d[["latency"]]))
	ylim[1] <- min(ylim[1], d[["latency"]])
	ylim[2] <- max(ylim[2], d[["latency"]] + 2)
}

rprintf("Graph: x(%0.2f, %0.2f)  y(%0.2f, %0.2f)\n", xlim[1], xlim[2], ylim[1], ylim[2])


if (output.file != FALSE) {
	pdf(output.file, width=15, height=9)
}

par(mfrow=c(3,1))

plot(c(), xlim=xlim, ylim=ylim, main="", xlab="Requests over time", ylab="Latency")
for (d in data) {
	points(d[["latency"]], col=d[["color"]], type="p", pch=20)
}

hist(c(0), xlim=ylim, ylim=c(xlim[1], xlim[2]/10), main="", xlab="Latency", ylab="Count")
legend("topright", NULL, data.titles, col=data.colors, pch=20)
for (d in data) {
	hist(d[["latency"]], border=d[["color"]], add=TRUE, breaks=100)
}


# Density with curves
breaks <- length(d[["latency"]])/20
max.densities <- sapply( data, function(d) {  max(hist(d[["latency"]], breaks=breaks, plot=FALSE )$density ) } )
density.ylim <- c(0, max(max.densities) * 1.5)
hist(c(0), xlim=ylim, ylim=density.ylim, main="", xlab="Latency", ylab="Frequency")
for (d in data) {
	x <- hist(d[["latency"]], breaks=breaks, plot=FALSE)
	lines(x$breaks, c(0, x$density), xlim=ylim, col=d[["color"]])
}

if (output.file != FALSE) {
	xxx <- dev.off()
}
