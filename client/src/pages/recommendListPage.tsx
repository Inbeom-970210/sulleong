import React, { useEffect, useState } from 'react';
import axios from 'axios';
import { Container, TextField } from '@mui/material';

import BeerCard from '../components/beerCard';
import style from '../styles/recommendListPage.module.css';

interface Beer {
	id: number;
	image: string;
	name: string;
	nameKor: string;
	abv: number;
	largeCategory: string;
	subCategory: string;
	country: string;
	score: number;
}
interface Props {
	setIsAuthenticated: React.Dispatch<React.SetStateAction<boolean>>;
}

function RecommendListPage({ setIsAuthenticated }: Props) {
	const PER_PAGE = 50;

	const [beerList, setBeerList] = useState<Beer[]>([]);

	const url = `https://api.punkapi.com/v2/beers?page=1&per_page=${PER_PAGE}`;
	useEffect(() => {
		axios.get(url).then((res) => {
			setBeerList(res.data);
		});
	}, []);

	return (
		<div className={style.recommendPage}>
			<Container sx={{ marginBottom: '30px' }}>
				<TextField
					className={style.textField}
					id="standard-read-only-input"
					defaultValue="사용자님의 취향에 맞는 술을 찾아보았어요"
					InputProps={{
						readOnly: true,
						className: style.input,
					}}
					variant="standard"
				/>
			</Container>
			<Container className={style.beerList}>
				<hr />
				<div className={style.cardContainer}>
					{beerList.map((beer) => (
						<BeerCard key={beer.id} beer={beer} />
					))}
				</div>
			</Container>
		</div>
	);
}

export default RecommendListPage;
