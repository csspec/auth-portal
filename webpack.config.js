const webpack = require('webpack');
const path = require('path');

const BUILD_DIR = path.resolve(__dirname, 'src/main/resources/static');
const APP_DIR = path.resolve(__dirname, 'src/main/js');

const config = {
	entry: {
		index: APP_DIR + '/index.jsx',
		signin: APP_DIR + '/signin.jsx',
		admin: APP_DIR + '/admin.jsx'
	},
	output: {
		path: BUILD_DIR,
		filename: '[name].build.js'
	},
	resolve: {
		extensions: ['', '.js', '.jsx']
	},
	module: {
		loaders: [
			{
				test: /\.jsx?/,
				include: APP_DIR,
				loader: 'babel'
			}
		]
	},
	devtool: 'source-map'
};

module.exports = config;


